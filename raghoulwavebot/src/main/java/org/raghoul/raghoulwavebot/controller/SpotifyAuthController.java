package org.raghoul.raghoulwavebot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.spotify_authorization_response.SpotifyAuthorizationResponseDto;
import org.raghoul.raghoulwavebot.mapper.spotify_authorization_response.SpotifyAuthorizationResponseMapper;
import org.raghoul.raghoulwavebot.model.spotify_authorization_response.SpotifyAuthorizationResponse;
import org.raghoul.raghoulwavebot.service.spotify_web_api_authorization.SpotifyWebApiAuthorizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/callback")
public class SpotifyAuthController {

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyAuthorizationResponseMapper spotifyAuthorizationResponseMapper;

    @GetMapping()
    public ModelAndView getCode(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state
    ) {
        SpotifyAuthorizationResponseDto spotifyAuthorizationResponseDTO = spotifyAuthorizationResponseMapper.entityToDto(
                SpotifyAuthorizationResponse.builder()
                        .code(code)
                        .state(state)
                        .build()
        );
        spotifyWebApiAuthorizationService.authorizationCode_Sync(spotifyAuthorizationResponseDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
