package org.raghoul.raghoulwavebot.dto.spotify_authorization_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyAuthorizationResponseDto {
    private String code;
    private String state;

    public String toString() {
        return "\nSpotifyResponseDTO(code=" + this.getCode() +
                ", state=" + this.getState() + ")\n";
    }
}
