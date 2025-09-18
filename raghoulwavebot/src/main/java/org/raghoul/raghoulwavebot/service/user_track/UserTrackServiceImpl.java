package org.raghoul.raghoulwavebot.service.user_track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.user_track.UserTrackDto;
import org.raghoul.raghoulwavebot.mapper.user_track.UserTrackMapper;
import org.raghoul.raghoulwavebot.model.composite_key.user_track.UserTrackId;
import org.raghoul.raghoulwavebot.model.user_track.UserTrack;
import org.raghoul.raghoulwavebot.repository.user_track.UserTrackRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTrackServiceImpl implements UserTrackService {

    private final UserTrackRepository userTrackRepository;
    private final UserTrackMapper userTrackMapper;

    @Override
    public UserTrackDto getById(UserTrackId id) {
        Optional<UserTrack> optionalOfUserTrack = userTrackRepository.findById(id);
        UserTrack userTrack = optionalOfUserTrack.orElse(null);
        return userTrackMapper.entityToDto(userTrack);
    }

    @Override
    public List<UserTrackDto> getAll() {
        List<UserTrack> userTrackList = userTrackRepository.findAll();
        return userTrackMapper.entityListToDtoList(userTrackList);
    }

    @Override
    public void add(UserTrackDto userDto) {
        UserTrack userTrack = userTrackMapper.dtoToEntity(userDto);
        userTrackRepository.save(userTrack);
    }

    @Override
    public void update(UserTrackDto userDto) {
        UserTrack userTrack = userTrackMapper.dtoToEntity(userDto);
        userTrackRepository.save(userTrack);
    }

    @Override
    public void deleteById(UserTrackId id) {
        userTrackRepository.deleteById(id);
    }
}
