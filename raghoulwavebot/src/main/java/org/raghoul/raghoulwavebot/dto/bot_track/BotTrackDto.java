package org.raghoul.raghoulwavebot.dto.bot_track;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotTrackDto {
    private Integer id;
    private String title;
    private String artist;
    private String album;
    private String spotifyId;
    private String youtubeId;

    public String toString() {
        return "\nTrackDto(id=" + this.getId() +
                ", title=" + this.getTitle() +
                ", artist=" + this.getArtist() +
                ", album=" + this.getAlbum() +
                ", spotifyId=" + this.getSpotifyId() +
                ", youtubeId=" + this.getYoutubeId() +
                ")\n";
    }
}
