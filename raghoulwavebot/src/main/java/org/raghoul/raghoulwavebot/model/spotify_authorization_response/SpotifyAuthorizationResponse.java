package org.raghoul.raghoulwavebot.model.spotify_authorization_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyAuthorizationResponse {
    private String code;
    private String state;
}
