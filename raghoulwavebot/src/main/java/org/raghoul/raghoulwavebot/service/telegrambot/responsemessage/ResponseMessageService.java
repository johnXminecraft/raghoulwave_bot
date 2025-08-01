package org.raghoul.raghoulwavebot.service.telegrambot.responsemessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public interface ResponseMessageService {
    SendMessage getResponseMessage(User user, String botState, String command);
    SendMessage getFillerResponseMessage(User user);
}
