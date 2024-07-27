package org.raghoul.raghoulwavebot.service.telegrambot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.UserDto;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.raghoul.raghoulwavebot.spotifyapi.SpotifyApiAuthorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final UserService userService;
    private final SpotifyApiAuthorization spotifyApiAuthorization;
    private final String redirectUriString;
    @Value("${raghoulwavebot.config.administrator.id}")
    private String administratorId;

    public SendMessage handleUpdate(Update update) {

        Message incomingMessage = update.getMessage();
        User user = incomingMessage.getFrom();

        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(incomingMessage.getChatId());

        if(Objects.equals(incomingMessage.getText(), "/start")) {

            List<UserDto> userDtoList = userService.getAll();

            if(
                    userDtoList.stream()
                            .anyMatch(userDto ->
                                    Objects.equals(userDto.getTelegramId(), user.getId()
            ))) {
                messageToSend.setText("You are already registered :)");
            }
            else {
                UserDto newUser = UserDto.builder()
                        .telegramId(user.getId())
                        .tag(user.getUserName())
                        .first(user.getFirstName())
                        .last(user.getLastName())
                        .lang(user.getLanguageCode())
                        .build();
                userService.add(newUser);

                String redirectUriString = spotifyApiAuthorization.authorizationCodeUri_Sync();

                messageToSend.setText("Registration is successful :) \n\n" + redirectUriString);
            }
        }
        else if(
                Objects.equals(incomingMessage.getText(), "getAll") &&
                        Objects.equals(Long.toString(user.getId()), administratorId)
        ) {
            List<UserDto> userDtoList = userService.getAll();
            String result = userDtoList.toString();
            messageToSend.setText(result);
        }
        else {
            messageToSend.setText("meow");
        }

        return messageToSend;
    }
}
