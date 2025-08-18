package org.raghoul.raghoulwavebot.service.telegrambotapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotApiService {
    BotApiMethod<?> onWebhookUpdateReceived(Update update);
    void onRegister();
}
