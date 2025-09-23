package org.raghoul.raghoulwavebot.service.bot_track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;
import org.raghoul.raghoulwavebot.repository.bot_track.BotTrackRepository;
import org.raghoul.raghoulwavebot.repository.bot_user_track.BotUserTrackRepository;
import org.raghoul.raghoulwavebot.service.bot_user_track.BotUserTrackService;
import org.raghoul.raghoulwavebot.service.spotify_web_api.SpotifyWebApiService;
import org.raghoul.raghoulwavebot.service.youtube_data_api.YoutubeDataApiService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotTrackServiceImpl implements BotTrackService {

    private final BotTrackRepository botTrackRepository;
    private final BotUserTrackRepository botUserTrackRepository;
    private final BotTrackMapper botTrackMapper;
    private final SpotifyWebApiService spotifyWebApiService;
    private final YoutubeDataApiService youtubeDataApiService;
    private final BotUserTrackService botUserTrackService;

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
    public BotTrackDto add(BotTrackDto botTrackDto) {
        return botTrackMapper.entityToDto(botTrackRepository.save(botTrackMapper.dtoToEntity(botTrackDto)));
    }

    @Override
    public BotTrackDto update(BotTrackDto botTrackDto) {
        return botTrackMapper.entityToDto(botTrackRepository.save(botTrackMapper.dtoToEntity(botTrackDto)));
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

    @Override
    public BotTrackDto spotifyTrackToBotTrackDto(BotUserDto botUserDto, Track track) {
        String title = Objects.requireNonNull(track).getName();
        String artist = Objects.requireNonNull(Arrays.stream(track.getArtists()).findFirst().orElse(null)).getName();
        String album = track.getAlbum().getName();
        String spotifyId = spotifyWebApiService.extractSpotifyTrackId(track.getUri());
        String youtubeId = youtubeDataApiService.getYtMusicTrackId(title + " " + artist);

        BotTrack botTrack = BotTrack.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .spotifyId(spotifyId)
                .youtubeId(youtubeId)
                .build();

        BotTrackDto botTrackDto = botTrackMapper.entityToDto(botTrack);
        botTrackDto = add(botTrackDto);

        // creating relation between track and user
        BotUserTrackId id = new BotUserTrackId(botUserDto.getId(), botTrackDto.getId(), "current");
        BotUserTrackDto botUserTrackDto = new BotUserTrackDto();
        botUserTrackDto.setId(id);
        botUserTrackDto.setBotUser(botUserDto);
        botUserTrackDto.setBotTrack(botTrackDto);
        botUserTrackService.add(botUserTrackDto);

        return botTrackDto;
    }
}
