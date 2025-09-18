package org.raghoul.raghoulwavebot.dto.track;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.user_track.UserTrackDto;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDto {
    private Integer id;
    private String title;
    private String artist;
    private String album;
    private String spotifyId;
    private String youtubeId;
    private Set<UserTrackDto> userTracks;

    public String toString() {
        return "\nTrackDto(id=" + this.getId() +
                ", title=" + this.getTitle() +
                ", artist=" + this.getArtist() +
                ", album=" + this.getAlbum() +
                ", spotifyId=" + this.getSpotifyId() +
                ", youtubeId=" + this.getYoutubeId() +
                ", userTracks=" + this.getUserTracks() +
                ")\n";
    }
}
