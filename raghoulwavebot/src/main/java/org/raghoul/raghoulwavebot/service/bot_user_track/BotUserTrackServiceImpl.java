package org.raghoul.raghoulwavebot.service.bot_user_track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import org.raghoul.raghoulwavebot.mapper.bot_user_track.BotUserTrackMapper;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;
import org.raghoul.raghoulwavebot.model.bot_user_track.BotUserTrack;
import org.raghoul.raghoulwavebot.repository.bot_user_track.BotUserTrackRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotUserTrackServiceImpl implements BotUserTrackService {

    private final BotUserTrackRepository botUserTrackRepository;
    private final BotUserTrackMapper botUserTrackMapper;

    @Override
    public BotUserTrackDto getById(BotUserTrackId id) {
        Optional<BotUserTrack> optionalOfUserTrack = botUserTrackRepository.findById(id);
        BotUserTrack botUserTrack = optionalOfUserTrack.orElse(null);
        return botUserTrackMapper.entityToDto(botUserTrack);
    }

    @Override
    public List<BotUserTrackDto> getAll() {
        List<BotUserTrack> botUserTrackList = botUserTrackRepository.findAll();
        return botUserTrackMapper.entityListToDtoList(botUserTrackList);
    }

    @Override
    public void add(BotUserTrackDto botUserTrackDto) {
        BotUserTrack botUserTrack = botUserTrackMapper.dtoToEntity(botUserTrackDto);
        botUserTrackRepository.save(botUserTrack);
    }

    @Override
    public void update(BotUserTrackDto botUserTrackDto) {
        BotUserTrack botUserTrack = botUserTrackMapper.dtoToEntity(botUserTrackDto);
        botUserTrackRepository.save(botUserTrack);
    }

    @Override
    public void deleteById(BotUserTrackId id) {
        botUserTrackRepository.deleteById(id);
    }
}
