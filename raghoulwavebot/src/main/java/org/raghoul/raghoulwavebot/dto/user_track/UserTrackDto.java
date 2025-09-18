package org.raghoul.raghoulwavebot.dto.user_track;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.track.TrackDto;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.model.composite_key.user_track.UserTrackId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTrackDto {
    private UserTrackId id;
    private UserDto user;
    private TrackDto track;

    public String toString() {
        return "\nTrackDto(id=" + this.getId() +
                ", userDto=" + this.getUser() +
                ", trackDto=" + this.getTrack() +
                ")\n";
    }
}
