package org.raghoul.raghoulwavebot.mapper.bot_track;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface BotTrackMapper {
    BotTrackDto entityToDto(BotTrack entity);
    BotTrack dtoToEntity(BotTrackDto dto);
    List<BotTrackDto> entityListToDtoList(List<BotTrack> entityList);
    List<BotTrack> dtoListToEntityList(List<BotTrackDto> dtoList);
}
