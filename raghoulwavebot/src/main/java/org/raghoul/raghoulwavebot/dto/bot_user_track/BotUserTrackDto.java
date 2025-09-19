package org.raghoul.raghoulwavebot.dto.bot_user_track;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotUserTrackDto {
    private BotUserTrackId id;
    private BotUserDto botUser;
    private BotTrackDto botTrack;

    public String toString() {
        return "\nTrackDto(id=" + this.getId() +
                ", botUser=" + this.getBotUser() +
                ", botTrack=" + this.getBotTrack() +
                ")\n";
    }
}
