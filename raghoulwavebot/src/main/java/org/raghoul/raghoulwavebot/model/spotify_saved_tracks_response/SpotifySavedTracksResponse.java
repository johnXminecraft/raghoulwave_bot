package org.raghoul.raghoulwavebot.model.spotify_saved_tracks_response;

import lombok.*;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifySavedTracksResponse {
    private Integer responseCode;
    private List<BotTrack> botTracks;
    private String output;
}
