package org.raghoul.raghoulwavebot.service.telegrambot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotService {
    SendMessage handleUpdate(Update update);
}
