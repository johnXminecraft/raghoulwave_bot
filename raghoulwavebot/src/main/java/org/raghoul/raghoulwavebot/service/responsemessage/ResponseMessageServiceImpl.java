package org.raghoul.raghoulwavebot.service.responsemessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.spotifywebapiauthorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.menu.MenuService;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${raghoulwavebot.config.administrator.id}")
    private String administratorId;

    @Override
    public SendMessage getResponseMessage(User user, String botState, String command) {
        System.out.println(botState + " " + command);
        return (Objects.equals(command, "/start"))
                ? startResponseMessage(user)
                : (Objects.equals(botState, "ready") && Objects.equals(command, "Ready"))
                ? readyResponseMessage(user, botState, command)
                : (Objects.equals(user.getId().toString(), administratorId) && Objects.equals(command, "getAll"))
                ? getAllResponseMessage(user)
                : (Objects.equals(botState, "ready") && Objects.equals(command, "Recent tracks"))
                ? getRecentlyPlayedTracksResponseMessage(user)
                : (Objects.equals(botState, "ready") && Objects.equals(command, "Liked tracks"))
                ? getSavedTracksResponseMessage(user)
                : (Objects.equals(botState, "ready") && Objects.equals(command, "Current track"))
                ? getCurrentTrackResponseMessage(user, botState, command)
                : (Objects.equals(botState, "currentTrack") && Objects.equals(command, "Download"))
                ? downloadCurrentTrackResponseMessage(user)
                : (Objects.equals(botState, "currentTrack") && Objects.equals(command, "Back"))
                ? mainMenuResponseMessage(user, command)
                : getFillerResponseMessage(user);
    }

    public SendMessage getFillerResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        messageToSend.setText("Didn't catch that, sorry.");
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage startResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        List<UserDto> userDtoList = userService.getAll();
        if(
           userDtoList.stream().anyMatch(userDto -> Objects.equals(userDto.getTelegramId(), user.getId()))
        ) {
            Optional<UserDto> userDtoOptional = userDtoList.stream()
                    .filter(userDtoFromList ->
                            Objects.equals(userDtoFromList.getTelegramId(), user.getId())).findFirst();
            UserDto userDto = userDtoOptional.orElseGet(UserDto::new);
            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(
                    userDto.getState());
            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "You may also try again!" + "</a>";
            messageToSend.setText("You are already registered :)\n\n" + hyperLink);
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
                    .botState("authorizing")
                    .build();
            userService.add(newUser);
            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(state);
            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "this link " + "</a>";
            messageToSend.setText("Almost done! Now you only need to log in with your Spotify account to finish " +
                    "the registration process. Use " + hyperLink + "to do so. \n\nAfter that, please press \"Ready\" " +
                    "button bellow. :)");
        }
        messageToSend.setChatId(user.getId());
        messageToSend.setReplyMarkup(menuService.startMenu());
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage readyResponseMessage(User user, String botState, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if(isUserRegistered(user)) {
            messageToSend.setText("You are fully ready to start experiencing Spotify from here :)");
            messageToSend.setReplyMarkup(menuService.getMenu(botState, command));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage mainMenuResponseMessage(User user, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if(isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            userDto.setBotState("ready");
            userService.update(userDto);
            messageToSend.setText("Please, choose an option :)");
            messageToSend.setReplyMarkup(menuService.getMenu(userDto.getBotState(), command));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    public SendMessage getAllResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        List<UserDto> userDtoList = userService.getAll();
        messageToSend.setChatId(user.getId());
        messageToSend.setText(userDtoList.toString());
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage getRecentlyPlayedTracksResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if (isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            messageToSend.setText(spotifyWebApiService.getRecentlyPlayedTracks(userDto));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage getSavedTracksResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if (isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            messageToSend.setText(spotifyWebApiService.getSavedTracks(userDto));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage getCurrentTrackResponseMessage(User user, String botState, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if (isUserRegistered(user)) {
            UserDto userDto = userService.getByTelegramId(user.getId());
            userDto.setBotState("currentTrack");
            userService.update(userDto);
            messageToSend.setText(spotifyWebApiService.getCurrentTrack(userDto));
            messageToSend.setReplyMarkup(menuService.getMenu(botState, command));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage downloadCurrentTrackResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        messageToSend.setText("This option is currently unavailable");
        messageToSend.enableHtml(true);
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
