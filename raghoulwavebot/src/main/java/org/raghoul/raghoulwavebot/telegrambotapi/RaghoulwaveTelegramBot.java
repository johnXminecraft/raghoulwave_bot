package org.raghoul.raghoulwavebot.telegrambotapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.handleupdate.HandleUpdateService;
import org.springframework.stereotype.Component;
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
@Component
public class RaghoulwaveTelegramBot extends SpringWebhookBot {

    /*TODO
    *  rewrite as a Service
    *  create Beans
    *  fix disability to send files
    * */

    private String botPath;
    private String botUsername;
    private String botToken;
    private final HandleUpdateService handleUpdateService;

    public RaghoulwaveTelegramBot(
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
