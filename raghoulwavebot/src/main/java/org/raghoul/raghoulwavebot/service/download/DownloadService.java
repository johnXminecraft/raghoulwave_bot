package org.raghoul.raghoulwavebot.service.download;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

public interface DownloadService {
    String sendTrack(BotUserDto botUserDto, IPlaylistItem item);
    String getYtMusicTrackId(String query);
}
