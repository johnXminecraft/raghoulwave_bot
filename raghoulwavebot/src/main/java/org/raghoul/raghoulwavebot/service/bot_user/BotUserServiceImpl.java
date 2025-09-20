package org.raghoul.raghoulwavebot.service.bot_user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.mapper.bot_user.BotUserMapper;
import org.raghoul.raghoulwavebot.model.bot_user.BotUser;
import org.raghoul.raghoulwavebot.repository.bot_user.BotUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {

    private final BotUserRepository botUserRepository;
    private final BotUserMapper botUserMapper;

    @Override
    public BotUserDto getById(Integer id) {
        Optional<BotUser> optionalOfUser = botUserRepository.findById(id);
        BotUser botUser = optionalOfUser.orElse(null);
        return botUserMapper.entityToDto(botUser);
    }

    @Override
    public List<BotUserDto> getAll() {
        List<BotUser> botUserList = botUserRepository.findAll();
        return botUserMapper.entityListToDtoList(botUserList);
    }

    @Override
    public BotUserDto add(BotUserDto botUserDto) {
        return botUserMapper.entityToDto(botUserRepository.save(botUserMapper.dtoToEntity(botUserDto)));
    }

    @Override
    public BotUserDto update(BotUserDto botUserDto) {
        return botUserMapper.entityToDto(botUserRepository.save(botUserMapper.dtoToEntity(botUserDto)));
    }

    @Override
    public void deleteById(Integer id) {
        botUserRepository.deleteById(id);
    }

    @Override
    public BotUserDto getByTelegramId(Long telegramId) {
        List<BotUser> usersByTelegramId = botUserRepository.findByTelegramId(telegramId);
        BotUser botUser = usersByTelegramId.getFirst();
        return botUserMapper.entityToDto(botUser);
    }

    @Override
    public BotUserDto getByState(String state) {
        List<BotUser> usersByState = botUserRepository.findByState(state);
        BotUser botUser = usersByState.getFirst();
        return botUserMapper.entityToDto(botUser);
    }

    @Override
    public boolean isUserRegistered(Long telegramId) {
        try {
            return !Objects.equals(getByTelegramId(telegramId).getRefreshToken(), "IRT");
        } catch (Exception e) {
            return false;
        }
    }
}
