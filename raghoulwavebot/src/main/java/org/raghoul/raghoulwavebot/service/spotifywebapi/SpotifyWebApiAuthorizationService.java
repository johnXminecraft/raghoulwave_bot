package org.raghoul.raghoulwavebot.service.spotifywebapi;

import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;

public interface SpotifyWebApiAuthorizationService {
    String authorizationCodeUri_Sync(String state);
    String authorizationCodeUri_Async(String state);
    String authorizationCode_Sync(SpotifyResponseDTO spotifyResponseDTO);
    String authorizationCode_Async();
    String authorizationCodeRefresh_Sync();
    String authorizationCodeRefresh_Async();
}
