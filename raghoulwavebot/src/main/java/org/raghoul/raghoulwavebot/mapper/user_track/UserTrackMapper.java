package org.raghoul.raghoulwavebot.mapper.user_track;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.raghoul.raghoulwavebot.dto.user_track.UserTrackDto;
import org.raghoul.raghoulwavebot.model.user_track.UserTrack;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserTrackMapper.class}
)
public interface UserTrackMapper {
    @Mapping(target = "user.userTracks", ignore = true)
    UserTrackDto entityToDto(UserTrack userTrack);
    @Mapping(target = "user.userTracks", ignore = true)
    UserTrack dtoToEntity(UserTrackDto userTrackDto);
    List<UserTrackDto> entityListToDtoList(List<UserTrack> userTracks);
    List<UserTrack> dtoListToEntityList(List<UserTrackDto> userTrackDtos);
}
