package org.raghoul.raghoulwavebot.dto.user;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.user_track.UserTrackDto;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
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
    private Set<UserTrackDto> userTracks;

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
                ", userTracks=" + this.getUserTracks() +
                ")\n";
    }
}
