package org.raghoul.raghoulwavebot.service.telegrambot.handleupdate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.telegrambot.responsemessage.ResponseMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class HandleUpdateServiceImpl implements HandleUpdateService {

    private final ResponseMessageService responseMessageService;
    @Value("${raghoulwavebot.config.administrator.id}")
    private String administratorId;

    public BotApiMethod<?> handleUpdate(Update update) {

        SendMessage filler = new SendMessage();
        filler.setChatId(administratorId);
        filler.setText("meow");
        BotApiMethod<?> answer = filler;

        if(update.hasMessage() && update.getMessage().hasText()) {
            answer = handleTextUpdate(update);
        }

        else if(update.hasCallbackQuery()) {
            answer = handleCallbackQuery(update);
        }

        return answer;
    }

    private SendMessage handleTextUpdate(Update update) {

        Message incomingMessage = update.getMessage();
        SendMessage messageToSend = new SendMessage();

        User user = incomingMessage.getFrom();
        messageToSend.setChatId(user.getId());
        messageToSend.enableHtml(true);

        if(Objects.equals(incomingMessage.getText(), "/start")) {
            messageToSend = responseMessageService.startResponseMessage(user, messageToSend);
        }

        else if(
                Objects.equals(incomingMessage.getText(), "getAll") &&
                        Objects.equals(Long.toString(user.getId()), administratorId)
        ) {
            messageToSend = responseMessageService.getAllResponseMessage(messageToSend);
        }

        else if(Objects.equals(incomingMessage.getText(), "Recent tracks")) {
            messageToSend = responseMessageService.getRecentlyPlayedTracksResponseMessage(user, messageToSend);
        }

        else if(Objects.equals(incomingMessage.getText(), "Saved tracks")) {
            messageToSend = responseMessageService.getSavedTracksResponseMessage(user, messageToSend);
        }

        else if(Objects.equals(incomingMessage.getText(), "Current track")) {
            messageToSend = responseMessageService.getCurrentTrackResponseMessage(user, messageToSend);
        }

        else {
            messageToSend = responseMessageService.fillerResponseMessage(messageToSend);
        }

        return messageToSend;
    }

    private SendMessage handleCallbackQuery(Update update) {

        SendMessage messageToSend = new SendMessage();

        User user = update.getCallbackQuery().getFrom();
        messageToSend.setChatId(user.getId());
        messageToSend.enableHtml(true);

        if(Objects.equals(update.getCallbackQuery().getData(), "Ready")) {
            messageToSend = responseMessageService.readyResponseMessage(user, messageToSend);
        }

        return messageToSend;
    }
}
