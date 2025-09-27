package org.raghoul.raghoulwavebot.model.spotify_recent_tracks_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyRecentTracksResponse {
    private Integer responseCode;
    private String output;
}
