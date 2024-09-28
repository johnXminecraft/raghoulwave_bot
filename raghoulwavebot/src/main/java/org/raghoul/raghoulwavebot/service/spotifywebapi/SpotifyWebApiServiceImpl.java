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

@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyWebApiServiceImpl implements SpotifyWebApiService {

    /*TODO
    *  Fix getRecentlyPlayedTracks()*/

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;

    @Override
    public String getRecentlyPlayedTracks(UserDto user) {

        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(user);

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        final GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest =
                spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                        .limit(5)
                        .build();

        try {
            final PagingCursorbased<PlayHistory> playHistoryPagingCursorbased =
                    getCurrentUsersRecentlyPlayedTracksRequest.execute();

            System.out.println(playHistoryPagingCursorbased.getTotal());

            return playHistoryPagingCursorbased.toString();
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }
}
