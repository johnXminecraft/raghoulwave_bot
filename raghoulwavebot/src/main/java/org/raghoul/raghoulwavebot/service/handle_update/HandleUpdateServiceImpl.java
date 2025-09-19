package org.raghoul.raghoulwavebot.service.handle_update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.response_message.ResponseMessageService;
import org.raghoul.raghoulwavebot.service.bot_user.BotUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@RequiredArgsConstructor
public class HandleUpdateServiceImpl implements HandleUpdateService {

    private final ResponseMessageService responseMessageService;
    private final BotUserService botUserService;

    public SendMessage handleUpdate(Update update) {
        User user;
        String botState;
        String command;
        SendMessage method;
        try {
            if(update.hasMessage() && update.getMessage().hasText()) {
                user = update.getMessage().getFrom();
                if(botUserService.isUserRegistered(user.getId())) {
                    botState = botUserService.getByTelegramId(user.getId()).getBotState();
                } else {
                    botState = "authorizing";
                }
                command = update.getMessage().getText();
                method = responseMessageService.getResponseMessage(user, botState, command);
            }
            else if(update.hasCallbackQuery()) {
                user = update.getCallbackQuery().getFrom();
                botState = botUserService.getByTelegramId(user.getId()).getBotState();
                command = update.getCallbackQuery().getData();
                method = responseMessageService.getResponseMessage(user, botState, command);
            }
            else {
                throw new TelegramApiException("Update message has no text");
            }
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return method;
    }
}
