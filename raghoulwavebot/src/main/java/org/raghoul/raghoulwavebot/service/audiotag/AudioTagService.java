package org.raghoul.raghoulwavebot.service.audiotag;

import org.raghoul.raghoulwavebot.dto.user.UserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

import java.io.File;

public interface AudioTagService {
    void setTrackTags(UserDto user, IPlaylistItem item, File file);
}
