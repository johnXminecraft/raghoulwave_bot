package org.raghoul.raghoulwavebot.service.spotifywebapi;

import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import java.io.IOException;
import java.util.Date;

@SuppressWarnings("StringBufferReplaceableByString")
@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyWebApiServiceImpl implements SpotifyWebApiService {

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;

    @Override
    public String getCurrentTrack(UserDto user) {
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(user);

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        GetUsersCurrentlyPlayingTrackRequest request = spotifyApi.getUsersCurrentlyPlayingTrack()
                .additionalTypes("track")
                .build();

        try {
            CurrentlyPlaying currentlyPlaying = request.execute();

            StringBuilder outputBuilder = new StringBuilder();

            outputBuilder
                    .append("<a href='")
                    .append(currentlyPlaying.getItem().getExternalUrls().get("spotify"))
                    .append("'>")
                    .append(currentlyPlaying.getItem().getName())
                    .append("</a>\n");

            String output = outputBuilder.toString();

            return "Current track:\n\n" + output;
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }

    @Override
    public String getSavedTracks(UserDto user) {

        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(user);

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        GetUsersSavedTracksRequest request = spotifyApi.getUsersSavedTracks().
                limit(16)
                .offset(0)
                .market(CountryCode.UA)
                .build();

        try {
            Paging<SavedTrack> savedTrackPaging = request.execute();

            SavedTrack[] savedTrack = savedTrackPaging.getItems();

            StringBuilder outputBuilder = new StringBuilder();

            for(SavedTrack savedTrackItem : savedTrack) {
                outputBuilder
                        .append("<a href='")
                        .append(savedTrackItem.getTrack().getExternalUrls().get("spotify"))
                        .append("'>")
                        .append(savedTrackItem.getTrack().getName())
                        .append("</a>\n");
            }

            String output = outputBuilder.toString();

            return "Saved tracks:\n\n" + output;
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }

    @Override
    public String getRecentlyPlayedTracks(UserDto user) {

        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(user);

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        long currentDate = new Date().getTime();

        GetCurrentUsersRecentlyPlayedTracksRequest request = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                        .before(new Date(currentDate))
                        .limit(10)
                        .build();

        try {
            PagingCursorbased<PlayHistory> playHistoryPagingCursorbased = request.execute();

            PlayHistory[] playHistory = playHistoryPagingCursorbased.getItems();

            StringBuilder outputBuilder = new StringBuilder();

            for(PlayHistory playHistoryItem : playHistory) {
                outputBuilder
                        .append("<a href='")
                        .append(playHistoryItem.getTrack().getExternalUrls().get("spotify"))
                        .append("'>")
                        .append(playHistoryItem.getTrack().getName())
                        .append("</a>\n");
            }

            String output = outputBuilder.toString();

            return "Recent tracks:\n\n" + output;
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }
}
