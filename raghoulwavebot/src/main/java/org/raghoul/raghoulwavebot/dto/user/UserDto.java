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
    private String lang;
    private String state;

    public String toString() {
        return "\nUserDto(id=" + this.getId() +
                ", telegramId=" + this.getTelegramId() +
                ", tag=" + this.getTag() +
                ", first=" + this.getFirst() +
                ", last=" + this.getLast() +
                ", lang=" + this.getLang() +
                ", state=" + this.getState() + ")\n";
    }
}
