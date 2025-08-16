package org.raghoul.raghoulwavebot.service.download;

import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

public interface DownloadService {
    SendDocument sendTrack(UserDto user, IPlaylistItem item);
    String downloadTrack(UserDto user, IPlaylistItem item);
}
