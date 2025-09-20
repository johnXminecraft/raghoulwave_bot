package org.raghoul.raghoulwavebot.service.response_message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.service.download.DownloadService;
import org.raghoul.raghoulwavebot.service.spotify_web_api.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.menu.MenuService;
import org.raghoul.raghoulwavebot.service.telegram_bot_api_authorization.TelegramBotApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.bot_user.BotUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseMessageServiceImpl implements ResponseMessageService {

    private final BotUserService botUserService;
    private final MenuService menuService;
    private final DownloadService downloadService;
    private final SpotifyWebApiService spotifyWebApiService;
    private final TelegramBotApiAuthorizationService telegramBotApiAuthorizationService;
    private final String administratorId;

    @Override
    public SendMessage getResponseMessage(User user, String botState, String command) {
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
        messageToSend.setText(telegramBotApiAuthorizationService.authorize(user));
        messageToSend.setChatId(user.getId());
        messageToSend.setReplyMarkup(menuService.startMenu());
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage readyResponseMessage(User user, String botState, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if(botUserService.isUserRegistered(user.getId())) {
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
        if(botUserService.isUserRegistered(user.getId())) {
            BotUserDto userDto = botUserService.getByTelegramId(user.getId());
            userDto.setBotState("ready");
            botUserService.update(userDto);
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
        List<BotUserDto> userDtoList = botUserService.getAll();
        messageToSend.setChatId(user.getId());
        messageToSend.setText(userDtoList.toString());
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage getRecentlyPlayedTracksResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if (botUserService.isUserRegistered(user.getId())) {
            BotUserDto userDto = botUserService.getByTelegramId(user.getId());
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
        if (botUserService.isUserRegistered(user.getId())) {
            BotUserDto userDto = botUserService.getByTelegramId(user.getId());
            messageToSend.setText(spotifyWebApiService.getSavedTracks(userDto));
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    // finish this one
    private SendMessage getCurrentTrackResponseMessage(User user, String botState, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        if (botUserService.isUserRegistered(user.getId())) {
            BotUserDto botUserDto = botUserService.getByTelegramId(user.getId());
            botUserDto.setBotState("currentTrack");
            botUserService.update(botUserDto);
            SpotifyCurrentTrackResponseDto spotifyCurrentTrackResponseDto = spotifyWebApiService.getCurrentTrack(botUserDto);
            messageToSend.setText(spotifyCurrentTrackResponseDto.getOutput());
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
        /*if (isUserRegistered(user)) {
            BotUserDto userDto = botUserService.getByTelegramId(user.getId());
            userDto.setBotState("currentTrack");
            botUserService.update(userDto);
            messageToSend.setText(downloadService.sendTrack(userDto, currentlyPlaying.getItem()))
            if(spotifyWebApiService.isSomethingPlayingCurrently(userDto)) {
                CurrentlyPlaying currentlyPlaying = spotifyWebApiService.getCurrentTrack(userDto);
                String output = downloadService.sendTrack(userDto, currentlyPlaying.getItem());
                messageToSend.setText(output);
            } else {
                messageToSend.setText("Nothing is playing :(");
            }
        } else {
            messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        }*/
        messageToSend.setText("Something went wrong, try registering again :(\n\nType /start to try again");
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private boolean isUserRegistered(User user) {
        try {
            botUserService.getByTelegramId(user.getId());
            return !Objects.equals(botUserService.getByTelegramId(user.getId()).getRefreshToken(), "IRT");
        } catch (Exception e) {
            return false;
        }
    }
}
