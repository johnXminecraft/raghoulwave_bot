package org.raghoul.raghoulwavebot.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.UserDto;
import org.raghoul.raghoulwavebot.mapper.UserMapper;
import org.raghoul.raghoulwavebot.model.User;
import org.raghoul.raghoulwavebot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getById(Integer id) {

        Optional<User> optionalOfUser = userRepository.findById(id);

        User user = optionalOfUser.orElse(null);

        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {

        List<User> userList = userRepository.findAll();

        return userMapper.userListToUserDtoList(userList);
    }

    @Override
    public void add(UserDto userDto) {

        User user = userMapper.userDtoToUser(userDto);

        userRepository.save(user);
    }

    @Override
    public void update(UserDto userDto) {

        User user = userMapper.userDtoToUser(userDto);

        userRepository.save(user);
    }

    @Override
    public void deleteById(Integer id) {

        userRepository.deleteById(id);
    }
}
