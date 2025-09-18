package org.raghoul.raghoulwavebot.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.mapper.user.UserMapper;
import org.raghoul.raghoulwavebot.model.user.User;
import org.raghoul.raghoulwavebot.repository.user.UserRepository;
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
        return userMapper.entityToDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userMapper.entityListToDtoList(userList);
    }

    @Override
    public void add(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        userRepository.save(user);
    }

    @Override
    public void update(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        userRepository.save(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getByTelegramId(Long telegramId) {
        List<User> usersByTelegramId = userRepository.findByTelegramId(telegramId);
        User user = usersByTelegramId.getFirst();
        return userMapper.entityToDto(user);
    }

    @Override
    public UserDto getByState(String state) {
        List<User> usersByState = userRepository.findByState(state);
        User user = usersByState.getFirst();
        return userMapper.entityToDto(user);
    }
}
