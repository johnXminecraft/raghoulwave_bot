package org.raghoul.raghoulwavebot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiAuthorizationService;
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

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return raghoulwaveTelegramBot.onWebhookUpdateReceived(update);
    }

    // TODO
    // ResponseEntity
    // Retrieving the Access Code via controller
}
