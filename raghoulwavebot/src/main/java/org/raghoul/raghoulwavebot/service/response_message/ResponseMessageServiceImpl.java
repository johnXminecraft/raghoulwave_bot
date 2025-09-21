package org.raghoul.raghoulwavebot.service.response_message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.download_track_response.DownloadTrackResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.service.bot_track.BotTrackService;
import org.raghoul.raghoulwavebot.service.download.DownloadService;
import org.raghoul.raghoulwavebot.service.spotify_web_api.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.menu.MenuService;
import org.raghoul.raghoulwavebot.service.telegram_bot_api_authorization.TelegramBotApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.bot_user.BotUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseMessageServiceImpl implements ResponseMessageService {

    private final BotUserService botUserService;
    private final MenuService menuService;
    private final SpotifyWebApiService spotifyWebApiService;
    private final TelegramBotApiAuthorizationService telegramBotApiAuthorizationService;
    private final String administratorId;
    private final BotTrackService botTrackService;
    private final DownloadService downloadService;

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

    private SendMessage getCurrentTrackResponseMessage(User user, String botState, String command) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        try {
            if (botUserService.isUserRegistered(user.getId())) {
                // getting user
                BotUserDto botUserDto = botUserService.getByTelegramId(user.getId());
                botUserDto.setBotState("currentTrack");
                botUserDto = botUserService.update(botUserDto);
                // deleting previous current track if there is such
                List<BotTrackDto> previousCurrentBotTracks = botTrackService.getByUserIdAndState(botUserDto.getId(), "current");
                if(!previousCurrentBotTracks.isEmpty()) {
                    previousCurrentBotTracks.forEach(botTrack -> botTrackService.deleteById(botTrack.getId()));
                }
                // getting new current track response
                SpotifyCurrentTrackResponseDto spotifyCurrentTrackResponseDto = spotifyWebApiService.getCurrentTrack(botUserDto);
                messageToSend.setText(spotifyCurrentTrackResponseDto.getOutput());
                messageToSend.setReplyMarkup(menuService.getMenu(botState, command));
            } else {
                /* TODO
                *   make custom exceptions */
                // BotUserNotRegisteredException
                throw new Exception("Something went wrong, try registering again :(\n\nType /start to try again");
            }
        } catch(Exception e) {
            messageToSend.setText(e.getMessage());
            messageToSend.enableHtml(true);
            return messageToSend;
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }

    private SendMessage downloadCurrentTrackResponseMessage(User user) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(user.getId());
        try {
            if (botUserService.isUserRegistered(user.getId())) {
                // getting user
                BotUserDto botUserDto = botUserService.getByTelegramId(user.getId());
                botUserDto.setBotState("currentTrack");
                botUserDto = botUserService.update(botUserDto);
                // getting track
                List<BotTrackDto> previousCurrentBotTracks = botTrackService.getByUserIdAndState(botUserDto.getId(), "current");
                BotTrackDto botTrackDto;
                if(!previousCurrentBotTracks.isEmpty()) {
                    botTrackDto = previousCurrentBotTracks.getFirst();
                } else {
                    /* TODO
                     *   make custom exceptions */
                    // NoCurrentTrackException
                    throw new Exception("Nothing is playing currently :(");
                }
                // downloading track
                DownloadTrackResponseDto downloadTrackResponseDto = downloadService.sendTrack(botUserDto, botTrackDto);
                messageToSend.setText(downloadTrackResponseDto.getOutput());
            }
        } catch(Exception e) {
            messageToSend.setText(e.getMessage());
        }
        messageToSend.enableHtml(true);
        return messageToSend;
    }
}
