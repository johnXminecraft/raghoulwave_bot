package org.raghoul.raghoulwavebot.service.telegrambot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    public SendMessage handleUpdate(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());

        if(Objects.equals(update.getMessage().getText(), "/spotify")) {
            message.setText("хаха хуй вам");
        }
        else {
            message.setText("meow");
        }

        return message;
    }
}
