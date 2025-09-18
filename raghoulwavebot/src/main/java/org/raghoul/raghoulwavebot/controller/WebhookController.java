package org.raghoul.raghoulwavebot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.telegram_bot_api.TelegramBotApiService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class WebhookController {

    private final TelegramBotApiService telegramBotApiService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotApiService.onWebhookUpdateReceived(update);
    }
}
