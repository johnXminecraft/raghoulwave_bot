package org.raghoul.raghoulwavebot.service.telegrambot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.user.UserService;
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
    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
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

                UserDto userDto = userDtoList.stream()
                        .filter(userDtoFromList ->
                                        Objects.equals(userDtoFromList.getTelegramId(), user.getId()))
                        .findFirst().get();

                String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(
                        userDto.getState()
                );

                messageToSend.setText("You are already registered :) \n" + redirectUriString);
            }

            else {

                String state = RandomStringUtils.random(16, true, false);

                UserDto newUser = UserDto.builder()
                        .telegramId(user.getId())
                        .tag(user.getUserName())
                        .first(user.getFirstName())
                        .last(user.getLastName())
                        .lang(user.getLanguageCode())
                        .state(state)
                        .build();
                userService.add(newUser);

                String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(state);

                messageToSend.setText("Registration is successful :) \n" +
                        "In order to authorize with your Spotify account, " +
                        "visit the following link. After that you will be " +
                        "all ready to use this bot. Just type \"Ready\" " +
                        "after visiting the link.\n" + redirectUriString);
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
