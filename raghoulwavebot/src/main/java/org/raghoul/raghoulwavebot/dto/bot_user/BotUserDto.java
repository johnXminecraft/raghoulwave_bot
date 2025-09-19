package org.raghoul.raghoulwavebot.dto.bot_user;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotUserDto {
    private Integer id;
    private Long telegramId;
    private String tag;
    private String first;
    private String last;
    private String language;
    private String state;
    private String refreshToken;
    private String botState;
    private Integer page;
    private Set<BotUserTrackDto> botUserTracks;

    public String toString() {
        return "\nUserDto(id=" + this.getId() +
                ", telegramId=" + this.getTelegramId() +
                ", tag=" + this.getTag() +
                ", first=" + this.getFirst() +
                ", last=" + this.getLast() +
                ", language=" + this.getLanguage() +
                ", state=" + this.getState() +
                ", refreshToken=" + this.getRefreshToken() +
                ", botState=" + this.getBotState() +
                ", botUserTracks=" + this.getBotUserTracks() +
                ")\n";
    }
}
