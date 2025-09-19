package org.raghoul.raghoulwavebot.service.bot_user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.mapper.bot_user.BotUserMapper;
import org.raghoul.raghoulwavebot.model.bot_user.BotUser;
import org.raghoul.raghoulwavebot.repository.bot_user.BotUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BotBotUserServiceImplTest {

    private final Integer ID = 1;

    @InjectMocks
    private BotUserServiceImpl testInstance;

    @Mock
    private BotUserRepository botUserRepository;
    @Mock
    private BotUserMapper botUserMapper;
    @Mock
    private BotUserDto userDto;
    @Mock
    private BotUser botUserEntity;

    @Test
    public void shouldReturnUserDtoById() {

        when(botUserRepository.findById(anyInt())).thenReturn(Optional.of(botUserEntity));

        testInstance.getById(ID);

        verify(botUserRepository).findById(ID);
    }

    @Test
    public void shouldNotReturnUserDtoById() {

        when(botUserRepository.findById(anyInt())).thenThrow(RuntimeException.class);

        testInstance.getById(ID);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldReturnListOfUserDtos() {

        List<BotUser> list = new ArrayList<>();

        when(botUserRepository.findAll()).thenReturn(list);

        testInstance.getAll();

        verify(botUserRepository).findAll();
    }

    @Test
    public void shouldNotReturnListOfUserDtos() {

        when(botUserRepository.findAll()).thenThrow(RuntimeException.class);

        testInstance.getAll();

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldAddUser() {

        when(botUserMapper.dtoToEntity(userDto)).thenReturn(botUserEntity);

        BotUser result = botUserMapper.dtoToEntity(userDto);

        testInstance.add(userDto);

        verify(botUserRepository).save(result);
    }

    @Test
    public void shouldNotAddUser() {

        when(botUserMapper.dtoToEntity(userDto)).thenThrow(RuntimeException.class);

        testInstance.add(userDto);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldUpdateUser() {

        when(botUserMapper.dtoToEntity(userDto)).thenReturn(botUserEntity);

        BotUser result = botUserMapper.dtoToEntity(userDto);

        testInstance.add(userDto);

        verify(botUserRepository).save(result);
    }

    @Test
    public void shouldNotUpdateUser() {

        when(botUserMapper.dtoToEntity(userDto)).thenThrow(RuntimeException.class);

        testInstance.add(userDto);

        assertThrowsExactly(RuntimeException.class, any());
    }

    @Test
    public void shouldDeleteUserById() {

        testInstance.deleteById(ID);

        verify(botUserRepository).deleteById(ID);
    }
}