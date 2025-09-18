package org.raghoul.raghoulwavebot.service.user_track;

import org.raghoul.raghoulwavebot.dto.user_track.UserTrackDto;
import org.raghoul.raghoulwavebot.model.composite_key.user_track.UserTrackId;

import java.util.List;

public interface UserTrackService {
    UserTrackDto getById(UserTrackId id);
    List<UserTrackDto> getAll();
    void add(UserTrackDto userTrackDto);
    void update(UserTrackDto userTrackDto);
    void deleteById(UserTrackId id);
}
