package org.raghoul.raghoulwavebot.service.telegrambot.handleupdate;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface HandleUpdateService {
    BotApiMethod<?> handleUpdate(Update update);
}
