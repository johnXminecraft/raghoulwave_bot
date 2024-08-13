package org.raghoul.raghoulwavebot.service.user;

import org.raghoul.raghoulwavebot.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(Integer id);
    List<UserDto> getAll();
    void add(UserDto userDto);
    void update(UserDto userDto);
    void deleteById(Integer id);
}
