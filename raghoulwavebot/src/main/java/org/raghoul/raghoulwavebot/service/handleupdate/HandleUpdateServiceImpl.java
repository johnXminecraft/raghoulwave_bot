package org.raghoul.raghoulwavebot.service.handleupdate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.service.responsemessage.ResponseMessageService;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class HandleUpdateServiceImpl implements HandleUpdateService {

    /*TODO
    * isUserRegistered(User user) should be external method.
    * */

    private final ResponseMessageService responseMessageService;
    private final UserService userService;

    public SendMessage handleUpdate(Update update) {
        User user;
        String botState;
        String command;
        SendMessage method;
        try {
            if(update.hasMessage() && update.getMessage().hasText()) {
                user = update.getMessage().getFrom();
                if(isUserRegistered(user)) {
                    botState = userService.getByTelegramId(user.getId()).getBotState();
                } else {
                    botState = "authorizing";
                }
                command = update.getMessage().getText();
                method = responseMessageService.getResponseMessage(user, botState, command);
            }
            else if(update.hasCallbackQuery()) {
                user = update.getCallbackQuery().getFrom();
                botState = userService.getByTelegramId(user.getId()).getBotState();
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

    private boolean isUserRegistered(User user) {
        try {
            userService.getByTelegramId(user.getId());
            return !Objects.equals(userService.getByTelegramId(user.getId()).getRefreshToken(), "IRT");
        } catch (Exception e) {
            return false;
        }
    }
}
