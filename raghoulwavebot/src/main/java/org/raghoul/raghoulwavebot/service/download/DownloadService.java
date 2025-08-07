package org.raghoul.raghoulwavebot.service.download;

import org.raghoul.raghoulwavebot.dto.user.UserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

public interface DownloadService {
    String getYtMusicTrackLink(UserDto user, IPlaylistItem item);
}
