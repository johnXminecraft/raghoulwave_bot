package org.raghoul.raghoulwavebot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.mapper.spotifyresponse.SpotifyResponseMapper;
import org.raghoul.raghoulwavebot.model.spotifyresponse.SpotifyResponse;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
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
    private final SpotifyResponseMapper spotifyResponseMapper;

    @GetMapping()
    public ModelAndView getCode(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state
    ) {
        SpotifyResponseDTO spotifyResponseDTO = spotifyResponseMapper.spotifyResponseToSpotifyResponseDTO(
                SpotifyResponse.builder()
                        .code(code)
                        .state(state)
                        .build()
        );
        spotifyWebApiAuthorizationService.authorizationCode_Sync(spotifyResponseDTO);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
