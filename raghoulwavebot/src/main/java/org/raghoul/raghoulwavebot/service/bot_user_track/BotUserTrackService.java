package org.raghoul.raghoulwavebot.service.bot_user_track;

import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;

import java.util.List;

public interface BotUserTrackService {
    BotUserTrackDto getById(BotUserTrackId id);
    List<BotUserTrackDto> getAll();
    BotUserTrackDto add(BotUserTrackDto botUserTrackDto);
    BotUserTrackDto update(BotUserTrackDto botUserTrackDto);
    void deleteById(BotUserTrackId id);
}
