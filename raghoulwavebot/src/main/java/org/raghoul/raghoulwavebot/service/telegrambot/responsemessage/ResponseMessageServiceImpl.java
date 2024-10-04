package org.raghoul.raghoulwavebot.service.telegrambot.responsemessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.telegrambot.menu.MenuService;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseMessageServiceImpl implements ResponseMessageService {

    private final UserService userService;
    private final MenuService menuService;
    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyWebApiService spotifyWebApiService;

    @Override
    public SendMessage startResponseMessage(User user, SendMessage messageToSend) {

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

            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "You may also try again!" + "</a>";

            messageToSend.setText("You are already registered :)\n\n" + hyperLink);
            messageToSend.setReplyMarkup(menuService.startMenu(redirectUriString));

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

            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "this link " + "</a>";

            messageToSend.setText("Almost done! Now you only need to log in with your Spotify account to finish " +
                    "the registration process. Use " + hyperLink + "to do so. \n\nAfter that, please press \"Ready\" " +
                    "button bellow. :)");
            messageToSend.setReplyMarkup(menuService.startMenu(redirectUriString));

            return messageToSend;
        }
    }

    @Override
    public SendMessage readyResponseMessage(User user, SendMessage messageToSend) {
        if(isUserRegistered(user)) {
            messageToSend.setText("You are fully ready to start experiencing Spotify from here :)");
            messageToSend.setReplyMarkup(menuService.readyMenu());
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        return messageToSend;
    }

    @Override
    public SendMessage getAllResponseMessage(SendMessage messageToSend) {
        List<UserDto> userDtoList = userService.getAll();
        messageToSend.setText(userDtoList.toString());
        return messageToSend;
    }

    @Override
    public SendMessage getRecentlyPlayedTracksResponseMessage(User user, SendMessage messageToSend) {
        if (isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            messageToSend.setText(spotifyWebApiService.getRecentlyPlayedTracks(userDto));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        return messageToSend;
    }

    @Override
    public SendMessage fillerResponseMessage(SendMessage messageToSend) {
        messageToSend.setText("meow");
        return messageToSend;
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
