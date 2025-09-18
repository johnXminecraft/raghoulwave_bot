package org.raghoul.raghoulwavebot.service.track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.track.TrackDto;
import org.raghoul.raghoulwavebot.mapper.track.TrackMapper;
import org.raghoul.raghoulwavebot.model.track.Track;
import org.raghoul.raghoulwavebot.repository.track.TrackRepository;
import org.raghoul.raghoulwavebot.repository.user_track.UserTrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final UserTrackRepository userTrackRepository;
    private final TrackMapper trackMapper;

    @Override
    public TrackDto getById(Integer id) {
        Optional<Track> trackOptional = trackRepository.findById(id);
        Track track = trackOptional.orElse(null);
        return trackMapper.entityToDto(track);
    }

    @Override
    public List<TrackDto> getAll() {
        List<Track> trackList = trackRepository.findAll();
        return trackMapper.entityListToDtoList(trackList);
    }

    @Override
    public void add(TrackDto trackDto) {
        trackRepository.save(trackMapper.dtoToEntity(trackDto));
    }

    @Override
    public void update(TrackDto trackDto) {
        trackRepository.save(trackMapper.dtoToEntity(trackDto));
    }

    @Override
    public void deleteById(Integer id) {
        trackRepository.deleteById(id);
    }

    @Override
    public List<TrackDto> getByTitle(String title) {
        List<Track> trackList = trackRepository.findByTitle(title);
        return trackMapper.entityListToDtoList(trackList);
    }

    @Override
    public List<TrackDto> getByArtist(String artist) {
        List<Track> trackList = trackRepository.findByArtist(artist);
        return trackMapper.entityListToDtoList(trackList);
    }

    @Override
    public List<TrackDto> getByAlbum(String album) {
        List<Track> trackList = trackRepository.findByAlbum(album);
        return trackMapper.entityListToDtoList(trackList);
    }

    @Override
    public TrackDto getBySpotifyId(String spotifyId) {
        Optional<Track> trackOptional = trackRepository.findBySpotifyId(spotifyId);
        Track track = trackOptional.orElse(null);
        return trackMapper.entityToDto(track);
    }

    @Override
    public TrackDto getByYoutubeId(String youtubeId) {
        Optional<Track> trackOptional = trackRepository.findByYoutubeId(youtubeId);
        Track track = trackOptional.orElse(null);
        return trackMapper.entityToDto(track);
    }

    @Override
    public List<TrackDto> getByUserIdAndState(Integer userId, String state) {
        List<Track> trackList = userTrackRepository.findTracksByUserIdAndState(userId, state);
        return trackMapper.entityListToDtoList(trackList);
    }

    @Override
    public List<TrackDto> getByUserId(Integer userId) {
        List<Track> trackList = userTrackRepository.findTracksByUserId(userId);
        return trackMapper.entityListToDtoList(trackList);
    }
}
