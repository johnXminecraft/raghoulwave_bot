package org.raghoul.raghoulwavebot.service.telegram_bot_api;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.handle_update.HandleUpdateService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TelegramBotApiServiceImpl extends SpringWebhookBot implements TelegramBotApiService {

    private String botPath;
    private String botUsername;
    private String botToken;
    private final HandleUpdateService handleUpdateService;

    public TelegramBotApiServiceImpl(
            DefaultBotOptions options,
            SetWebhook setWebhook,
            String botToken,
            HandleUpdateService handleUpdateService
    ) {
        super(options, setWebhook, botToken);
        this.handleUpdateService = handleUpdateService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return handleUpdateService.handleUpdate(update);
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
