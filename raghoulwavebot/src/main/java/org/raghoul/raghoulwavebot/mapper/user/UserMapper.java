package org.raghoul.raghoulwavebot.mapper.user;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.mapper.user_track.UserTrackMapper;
import org.raghoul.raghoulwavebot.model.user.User;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserTrackMapper.class}
)
public interface UserMapper {
    UserDto entityToDto(User entity);
    User dtoToEntity(UserDto dto);
    List<UserDto> entityListToDtoList(List<User> entityList);
    List<User> dtoListToEntityList(List<UserDto> dtoList);
}
