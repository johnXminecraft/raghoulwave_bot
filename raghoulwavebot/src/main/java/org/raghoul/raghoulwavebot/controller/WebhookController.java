package org.raghoul.raghoulwavebot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.mapper.spotifyresponse.SpotifyResponseMapper;
import org.raghoul.raghoulwavebot.model.spotifyresponse.SpotifyResponse;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.telegrambotapi.RaghoulwaveTelegramBot;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class WebhookController {

    private final RaghoulwaveTelegramBot raghoulwaveTelegramBot;
    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyResponseMapper spotifyResponseMapper;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return raghoulwaveTelegramBot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/api/callback")
    public String getCode(
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

        return "<a href=\"https://t.me/raghoulwave_bot\">Back to Telegram</a>";
    }
}
