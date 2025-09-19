package org.raghoul.raghoulwavebot.service.spotify_web_api_authorization;

import org.raghoul.raghoulwavebot.dto.spotify_authorization_response.SpotifyAuthorizationResponseDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;

public interface SpotifyWebApiAuthorizationService {
    String authorizationCodeUri_Sync(String state);
    String authorizationCodeUri_Async(String state);
    void authorizationCode_Sync(SpotifyAuthorizationResponseDto spotifyResponseDTO);
    String authorizationCode_Async();
    String authorizationCodeRefresh_Sync(BotUserDto botUserDto);
    String authorizationCodeRefresh_Async();
}
