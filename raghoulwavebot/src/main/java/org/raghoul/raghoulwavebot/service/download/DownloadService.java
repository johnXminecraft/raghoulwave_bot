package org.raghoul.raghoulwavebot.service.download;

import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.download_track_response.DownloadTrackResponseDto;

public interface DownloadService {
    DownloadTrackResponseDto sendTrack(BotUserDto botUserDto, BotTrackDto botTrackDto);
}
