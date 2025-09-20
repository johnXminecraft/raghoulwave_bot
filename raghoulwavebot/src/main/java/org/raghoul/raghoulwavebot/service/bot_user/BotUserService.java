package org.raghoul.raghoulwavebot.service.bot_user;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;

import java.util.List;

public interface BotUserService {
    BotUserDto getById(Integer id);
    List<BotUserDto> getAll();
    BotUserDto add(BotUserDto botUserDto);
    BotUserDto update(BotUserDto botUserDto);
    void deleteById(Integer id);
    BotUserDto getByTelegramId(Long telegramId);
    BotUserDto getByState(String state);
    boolean isUserRegistered(Long telegramId);
}
