package org.raghoul.raghoulwavebot.service.audiotag;

import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import java.io.File;

public interface AudioTagService {
    void setTrackTags(BotTrackDto botTrackDto, File file);
}
