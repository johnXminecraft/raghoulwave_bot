package org.raghoul.raghoulwavebot.service.spotifywebapiauthorization;

import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.dto.user.UserDto;

public interface SpotifyWebApiAuthorizationService {
    String authorizationCodeUri_Sync(String state);
    String authorizationCodeUri_Async(String state);
    void authorizationCode_Sync(SpotifyResponseDTO spotifyResponseDTO);
    String authorizationCode_Async();
    String authorizationCodeRefresh_Sync(UserDto user);
    String authorizationCodeRefresh_Async();
}
