package org.raghoul.raghoulwavebot.model.composite_key.bot_user_track;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotUserTrackId implements Serializable {
    private Integer botUserId;
    private Integer botTrackId;
    private String state;
}

