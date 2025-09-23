package org.raghoul.raghoulwavebot.dto.spotify_saved_tracks_response;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifySavedTracksResponseDto {
    private Integer responseCode;
    private List<BotTrackDto> botTracks;
    private String output;

    @Override
    public String toString() {
        return "\nSpotifyResponseDTO(responseCode=" + this.getResponseCode() +
                ", botTracks=" + this.getBotTracks() +
                ", output=" + this.getOutput() +
                ")";
    }
}
