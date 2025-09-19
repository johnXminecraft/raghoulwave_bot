package org.raghoul.raghoulwavebot.service.audiotag;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

import java.io.File;

public interface AudioTagService {
    void setTrackTags(BotUserDto botUser, IPlaylistItem item, File file);
}
