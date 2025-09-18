package org.raghoul.raghoulwavebot.dto.spotify_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyResponseDTO {
    private String code;
    private String state;

    public String toString() {
        return "\nSpotifyResponseDTO(code=" + this.getCode() +
                ", state=" + this.getState() + ")\n";
    }
}
