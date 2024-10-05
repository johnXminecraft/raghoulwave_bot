package org.raghoul.raghoulwavebot.service.telegrambot.responsemessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public interface ResponseMessageService {
    SendMessage startResponseMessage(User user, SendMessage messageToSend);
    SendMessage readyResponseMessage(User user, SendMessage messageToSend);
    SendMessage getAllResponseMessage(SendMessage messageToSend);
    SendMessage getRecentlyPlayedTracksResponseMessage(User user, SendMessage messageToSend);
    SendMessage getSavedTracksResponseMessage(User user, SendMessage messageToSend);
    SendMessage fillerResponseMessage(SendMessage messageToSend);
}
