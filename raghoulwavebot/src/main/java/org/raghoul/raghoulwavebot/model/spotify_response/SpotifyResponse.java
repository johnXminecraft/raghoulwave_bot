package org.raghoul.raghoulwavebot.model.spotify_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyResponse {
    private String code;
    private String state;
}
