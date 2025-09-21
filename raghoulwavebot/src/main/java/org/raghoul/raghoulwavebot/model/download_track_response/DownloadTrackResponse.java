package org.raghoul.raghoulwavebot.model.download_track_response;

import lombok.*;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadTrackResponse {
    private Integer responseCode;
    private BotTrack botTrack;
    private String output;
}
