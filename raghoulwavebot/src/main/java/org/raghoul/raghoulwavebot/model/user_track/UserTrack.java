package org.raghoul.raghoulwavebot.model.user_track;

import jakarta.persistence.*;
import lombok.*;
import org.raghoul.raghoulwavebot.model.composite_key.user_track.UserTrackId;
import org.raghoul.raghoulwavebot.model.track.Track;
import org.raghoul.raghoulwavebot.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_tracks")
public class UserTrack {
    @EmbeddedId
    private UserTrackId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trackId")
    private Track track;
}
