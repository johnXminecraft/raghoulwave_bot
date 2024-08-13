package org.raghoul.raghoulwavebot.mapper.user;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.model.user.User;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {

    UserDto userToUserDto(User entity);

    User userDtoToUser(UserDto dto);

    List<UserDto> userListToUserDtoList(List<User> entityList);
}
