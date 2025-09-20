package org.raghoul.raghoulwavebot.dto.spotify_current_track_response;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyCurrentTrackResponseDto {
    private Integer responseCode;
    private BotTrackDto botTrack;
    private String output;

    @Override
    public String toString() {
        return "\nSpotifyResponseDTO(responseCode=" + this.getResponseCode() +
                ", botTrack=" + this.getBotTrack() +
                ", output=" + this.getOutput() +
                ")";
    }
}
