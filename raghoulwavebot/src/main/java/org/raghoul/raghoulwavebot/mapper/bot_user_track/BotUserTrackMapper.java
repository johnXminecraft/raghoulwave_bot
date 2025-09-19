package org.raghoul.raghoulwavebot.mapper.bot_user_track;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import org.raghoul.raghoulwavebot.model.bot_user_track.BotUserTrack;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {BotUserTrackMapper.class}
)
public interface BotUserTrackMapper {
    @Mapping(target = "botUser.botUserTracks", ignore = true)
    BotUserTrackDto entityToDto(BotUserTrack botUserTrack);
    @Mapping(target = "botUser.botUserTracks", ignore = true)
    BotUserTrack dtoToEntity(BotUserTrackDto botUserTrackDto);
    List<BotUserTrackDto> entityListToDtoList(List<BotUserTrack> botUserTracks);
    List<BotUserTrack> dtoListToEntityList(List<BotUserTrackDto> botUserTrackDtos);
}
