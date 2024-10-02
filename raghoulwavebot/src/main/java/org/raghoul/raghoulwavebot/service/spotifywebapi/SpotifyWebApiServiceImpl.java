package org.raghoul.raghoulwavebot.service.spotifywebapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyWebApiServiceImpl implements SpotifyWebApiService {

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;

    @Override
    public String getRecentlyPlayedTracks(UserDto user) {

        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(user);

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        long currentDate = new Date().getTime();

        GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest =
                spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                        .before(new Date(currentDate))
                        .limit(10)
                        .build();

        try {
            PagingCursorbased<PlayHistory> playHistoryPagingCursorbased =
                    getCurrentUsersRecentlyPlayedTracksRequest.execute();

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
