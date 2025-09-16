package org.raghoul.raghoulwavebot.dto.user;

import lombok.*;

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
                ")\n";
    }
}
