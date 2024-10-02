package org.raghoul.raghoulwavebot.service.telegrambot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final UserService userService;
    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyWebApiService spotifyWebApiService;
    @Value("${raghoulwavebot.config.administrator.id}")
    private String administratorId;

    public SendMessage handleUpdate(Update update) {

        Message incomingMessage = update.getMessage();
        User user = incomingMessage.getFrom();

        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(incomingMessage.getChatId());
        messageToSend.enableHtml(true);

        if(Objects.equals(incomingMessage.getText(), "/start")) {
            messageToSend = startResponseMessage(user, messageToSend);
        }

        else if(Objects.equals(incomingMessage.getText(), "Ready")) {
            messageToSend = readyResponseMessage(user, messageToSend);
        }

        else if(
                Objects.equals(incomingMessage.getText(), "getAll") &&
                        Objects.equals(Long.toString(user.getId()), administratorId)
        ) {
            messageToSend = getAllResponseMessage(messageToSend);
        }

        else if(Objects.equals(incomingMessage.getText(), "Recent tracks")) {
            messageToSend = getRecentlyPlayedTracksResponseMessage(user, messageToSend);
        }

        else {
            messageToSend = fillerResponseMessage(messageToSend);
        }

        return messageToSend;
    }

    private SendMessage startResponseMessage(User user, SendMessage messageToSend) {

        List<UserDto> userDtoList = userService.getAll();

        if(
                userDtoList.stream()
                        .anyMatch(userDto ->
                                Objects.equals(userDto.getTelegramId(), user.getId()
                                ))) {
            Optional<UserDto> userDtoOptional = userDtoList.stream()
                    .filter(userDtoFromList ->
                            Objects.equals(userDtoFromList.getTelegramId(), user.getId()))
                    .findFirst();

            UserDto userDto = userDtoOptional.orElseGet(UserDto::new);

            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(
                    userDto.getState()
            );

            String hyperLink = "<a href='" + redirectUriString + "'>Register</a>";

            messageToSend.setText("You are already registered :) \n\n" + hyperLink);

            return messageToSend;
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
                    .refreshToken("IRT")
                    .build();
            userService.add(newUser);

            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(state);

            String hyperLink = "<a href='" + redirectUriString + "'>this link</a>";

            messageToSend.setText("Registration is successful :) \n" +
                    "In order to authorize with your Spotify account, " +
                    "visit " + hyperLink + ". After that you will be " +
                    "all ready to use this bot. Just type \"Ready\" " +
                    "after visiting the link.");

            return messageToSend;
        }
    }

    private SendMessage readyResponseMessage(User user, SendMessage messageToSend) {
        if(isUserRegistered(user)) {
            messageToSend.setText("You are fully ready to start experiencing Spotify from here :)");
            return messageToSend;
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
            return messageToSend;
        }
    }

    private SendMessage getAllResponseMessage(SendMessage messageToSend) {
        List<UserDto> userDtoList = userService.getAll();
        messageToSend.setText(userDtoList.toString());
        return messageToSend;
    }

    private SendMessage getRecentlyPlayedTracksResponseMessage(User user, SendMessage messageToSend) {
        if (isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            messageToSend.setText(spotifyWebApiService.getRecentlyPlayedTracks(userDto));
            return messageToSend;
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
            return messageToSend;
        }
    }

    private SendMessage fillerResponseMessage(SendMessage messageToSend) {
        messageToSend.setText("meow");
        return messageToSend;
    }

    private boolean isUserRegistered(User user) {
        try {
            userService.getByTelegramId(user.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
