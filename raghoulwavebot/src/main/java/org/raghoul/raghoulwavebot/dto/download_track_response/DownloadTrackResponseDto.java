package org.raghoul.raghoulwavebot.dto.download_track_response;

import lombok.*;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadTrackResponseDto {
    private Integer responseCode;
    private BotTrackDto botTrack;
    private String output;

    @Override
    public String toString() {
        return "\nDownloadTrackResponseDto(responseCode=" + this.getResponseCode() +
                ", botTrack=" + this.getBotTrack() +
                ", output=" + this.getOutput() +
                ")";
    }
}
