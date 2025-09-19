package org.raghoul.raghoulwavebot.service.telegram_bot_api_authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.service.spotify_web_api_authorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.bot_user.BotUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotApiAuthorizationServiceImpl implements TelegramBotApiAuthorizationService {

    private final BotUserService botUserService;
    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;

    public String authorize(User user) {
        List<BotUserDto> userDtoList = botUserService.getAll();
        if(
                userDtoList.stream().anyMatch(userDto -> Objects.equals(userDto.getTelegramId(), user.getId()))
        ) {
            Optional<BotUserDto> userDtoOptional = userDtoList.stream()
                    .filter(userDtoFromList ->
                            Objects.equals(userDtoFromList.getTelegramId(), user.getId())).findFirst();
            BotUserDto userDto = userDtoOptional.orElseGet(BotUserDto::new);
            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(
                    userDto.getState());
            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "You may also try again!" + "</a>";
            return "You are already registered :)\n\n" + hyperLink;
        }
        else {
            String state = RandomStringUtils.random(16, true, false);
            BotUserDto newUser = BotUserDto.builder()
                    .telegramId(user.getId())
                    .tag(user.getUserName())
                    .first(user.getFirstName())
                    .last(user.getLastName())
                    .language(user.getLanguageCode())
                    .state(state)
                    .refreshToken("IRT")
                    .botState("authorizing")
                    .page(0)
                    .build();
            botUserService.add(newUser);
            String redirectUriString = spotifyWebApiAuthorizationService.authorizationCodeUri_Sync(state);
            String hyperLink = "<a href=\"" + redirectUriString + "\">" + "this link " + "</a>";
             return "Almost done! Now you only need to log in with your Spotify account to finish " +
                    "the registration process. Use " + hyperLink + "to do so. \n\nAfter that, please press \"Ready\" " +
                    "button bellow. :)";
        }
    }
}
