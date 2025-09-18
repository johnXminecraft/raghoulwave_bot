package org.raghoul.raghoulwavebot.mapper.track;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.track.TrackDto;
import org.raghoul.raghoulwavebot.mapper.user_track.UserTrackMapper;
import org.raghoul.raghoulwavebot.model.track.Track;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserTrackMapper.class}
)
public interface TrackMapper {
    TrackDto entityToDto(Track entity);
    Track dtoToEntity(TrackDto dto);
    List<TrackDto> entityListToDtoList(List<Track> entityList);
    List<Track> dtoListToEntityList(List<TrackDto> dtoList);
}
