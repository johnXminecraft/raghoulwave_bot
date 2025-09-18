package org.raghoul.raghoulwavebot.model.composite_key.user_track;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTrackId implements Serializable {
    private Integer userId;
    private Integer trackId;
    private String state;
}

