package org.raghoul.raghoulwavebot.dto.spotify_recent_tracks_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyRecentTracksResponseDto {
    private Integer responseCode;
    private String output;

    @Override
    public String toString() {
        return "\nSpotifyResponseDTO(responseCode=" + this.getResponseCode() +
                ", output=" + this.getOutput() +
                ")";
    }
}
