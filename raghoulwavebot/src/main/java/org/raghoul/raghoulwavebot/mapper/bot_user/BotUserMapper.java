package org.raghoul.raghoulwavebot.mapper.bot_user;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.mapper.bot_user_track.BotUserTrackMapper;
import org.raghoul.raghoulwavebot.model.bot_user.BotUser;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {BotUserTrackMapper.class}
)
public interface BotUserMapper {
    BotUserDto entityToDto(BotUser entity);
    BotUser dtoToEntity(BotUserDto dto);
    List<BotUserDto> entityListToDtoList(List<BotUser> entityList);
    List<BotUser> dtoListToEntityList(List<BotUserDto> dtoList);
}
