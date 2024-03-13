package org.raghoul.raghoulwavebot.service.telegrambot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotService {
    BotApiMethod<?> handleUpdate(Update update);
}
