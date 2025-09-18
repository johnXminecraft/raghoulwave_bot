package org.raghoul.raghoulwavebot.service.response_message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public interface ResponseMessageService {
    SendMessage getResponseMessage(User user, String botState, String command);
    SendMessage getFillerResponseMessage(User user);
}
