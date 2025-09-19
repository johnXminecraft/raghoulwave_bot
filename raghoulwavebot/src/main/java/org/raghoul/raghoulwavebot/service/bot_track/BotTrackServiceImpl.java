package org.raghoul.raghoulwavebot.service.bot_track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.repository.bot_track.BotTrackRepository;
import org.raghoul.raghoulwavebot.repository.bot_user_track.BotUserTrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotTrackServiceImpl implements BotTrackService {

    private final BotTrackRepository botTrackRepository;
    private final BotUserTrackRepository botUserTrackRepository;
    private final BotTrackMapper botTrackMapper;

    @Override
    public BotTrackDto getById(Integer id) {
        Optional<BotTrack> trackOptional = botTrackRepository.findById(id);
        BotTrack botTrack = trackOptional.orElse(null);
        return botTrackMapper.entityToDto(botTrack);
    }

    @Override
    public List<BotTrackDto> getAll() {
        List<BotTrack> botTrackList = botTrackRepository.findAll();
        return botTrackMapper.entityListToDtoList(botTrackList);
    }

    @Override
    public void add(BotTrackDto botTrackDto) {
        botTrackRepository.save(botTrackMapper.dtoToEntity(botTrackDto));
    }

    @Override
    public void update(BotTrackDto botTrackDto) {
        botTrackRepository.save(botTrackMapper.dtoToEntity(botTrackDto));
    }

    @Override
    public void deleteById(Integer id) {
        botTrackRepository.deleteById(id);
    }

    @Override
    public List<BotTrackDto> getByTitle(String title) {
        List<BotTrack> botTrackList = botTrackRepository.findByTitle(title);
        return botTrackMapper.entityListToDtoList(botTrackList);
    }

    @Override
    public List<BotTrackDto> getByArtist(String artist) {
        List<BotTrack> botTrackList = botTrackRepository.findByArtist(artist);
        return botTrackMapper.entityListToDtoList(botTrackList);
    }

    @Override
    public List<BotTrackDto> getByAlbum(String album) {
        List<BotTrack> botTrackList = botTrackRepository.findByAlbum(album);
        return botTrackMapper.entityListToDtoList(botTrackList);
    }

    @Override
    public BotTrackDto getBySpotifyId(String spotifyId) {
        Optional<BotTrack> trackOptional = botTrackRepository.findBySpotifyId(spotifyId);
        BotTrack botTrack = trackOptional.orElse(null);
        return botTrackMapper.entityToDto(botTrack);
    }

    @Override
    public BotTrackDto getByYoutubeId(String youtubeId) {
        Optional<BotTrack> trackOptional = botTrackRepository.findByYoutubeId(youtubeId);
        BotTrack botTrack = trackOptional.orElse(null);
        return botTrackMapper.entityToDto(botTrack);
    }

    @Override
    public List<BotTrackDto> getByUserIdAndState(Integer botUserId, String state) {
        List<BotTrack> botTrackList = botUserTrackRepository.findTracksByUserIdAndState(botUserId, state);
        return botTrackMapper.entityListToDtoList(botTrackList);
    }

    @Override
    public List<BotTrackDto> getByUserId(Integer botUserId) {
        List<BotTrack> botTrackList = botUserTrackRepository.findTracksByUserId(botUserId);
        return botTrackMapper.entityListToDtoList(botTrackList);
    }
}
