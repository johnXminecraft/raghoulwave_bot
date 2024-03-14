package org.raghoul.raghoulwavebot.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raghoul.raghoulwavebot.dto.UserDto;
import org.raghoul.raghoulwavebot.mapper.UserMapper;
import org.raghoul.raghoulwavebot.model.User;
import org.raghoul.raghoulwavebot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {

    private final Integer ID = 1;

    @InjectMocks
    private UserServiceImpl testInstance;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDto userDto;
    @Mock
    private User userEntity;

    @Test
    public void shouldReturnUserDtoById() {

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        testInstance.getById(ID);

        verify(userRepository).findById(ID);
    }

    @Test
    public void shouldNotReturnUserDtoById() {

        when(userRepository.findById(anyInt())).thenThrow(RuntimeException.class);

        testInstance.getById(ID);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldReturnListOfUserDtos() {

        List<User> list = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(list);

        testInstance.getAll();

        verify(userRepository).findAll();
    }

    @Test
    public void shouldNotReturnListOfUserDtos() {

        when(userRepository.findAll()).thenThrow(RuntimeException.class);

        testInstance.getAll();

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldAddUser() {

        when(userMapper.userDtoToUser(userDto)).thenReturn(userEntity);

        User result = userMapper.userDtoToUser(userDto);

        testInstance.add(userDto);

        verify(userRepository).save(result);
    }

    @Test
    public void shouldNotAddUser() {

        when(userMapper.userDtoToUser(userDto)).thenThrow(RuntimeException.class);

        testInstance.add(userDto);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldUpdateUser() {

        when(userMapper.userDtoToUser(userDto)).thenReturn(userEntity);

        User result = userMapper.userDtoToUser(userDto);

        testInstance.add(userDto);

        verify(userRepository).save(result);
    }

    @Test
    public void shouldNotUpdateUser() {

        when(userMapper.userDtoToUser(userDto)).thenThrow(RuntimeException.class);

        testInstance.add(userDto);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldDeleteUserById() {

        testInstance.deleteById(ID);

        verify(userRepository).deleteById(ID);
    }
}