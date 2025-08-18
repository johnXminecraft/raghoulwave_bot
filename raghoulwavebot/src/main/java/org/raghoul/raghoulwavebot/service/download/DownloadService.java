package org.raghoul.raghoulwavebot.service.download;

import org.raghoul.raghoulwavebot.dto.user.UserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

public interface DownloadService {
    String sendTrack(UserDto user, IPlaylistItem item);
}
