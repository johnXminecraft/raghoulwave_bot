package org.raghoul.raghoulwavebot.model.bot_user_track;

import jakarta.persistence.*;
import lombok.*;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.model.bot_user.BotUser;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bot_users_tracks")
public class BotUserTrack {
    @EmbeddedId
    private BotUserTrackId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("botUserId")
    private BotUser botUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("botTrackId")
    private BotTrack botTrack;
}
