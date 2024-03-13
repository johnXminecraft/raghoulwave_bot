package org.raghoul.raghoulwavebot.mapper;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.UserDto;
import org.raghoul.raghoulwavebot.model.User;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {

    UserDto userToUserDto(User entity);

    User userDtoToUser(UserDto dto);

    List<UserDto> userListToUserDtoList(List<User> entityList);
}
