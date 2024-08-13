package org.raghoul.raghoulwavebot.model.spotifyresponse;

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
