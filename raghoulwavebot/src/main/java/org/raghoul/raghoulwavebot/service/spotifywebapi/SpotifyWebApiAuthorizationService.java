package org.raghoul.raghoulwavebot.service.spotifywebapi;

public interface SpotifyWebApiAuthorizationService {
    String authorizationCodeUri_Sync();
    String authorizationCodeUri_Async();
    String authorizationCode_Sync();
    String authorizationCode_Async();
    String authorizationCodeRefresh_Sync();
    String authorizationCodeRefresh_Async();
}
