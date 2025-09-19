package org.raghoul.raghoulwavebot.model.spotify_current_track_response;

import lombok.*;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyCurrentTrackResponse {
    private boolean exist;
    private BotTrack botTrack;
    private String output;
}
