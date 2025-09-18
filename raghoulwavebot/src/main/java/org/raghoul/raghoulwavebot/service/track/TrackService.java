package org.raghoul.raghoulwavebot.service.track;

import org.raghoul.raghoulwavebot.dto.track.TrackDto;
import java.util.List;

public interface TrackService {
    TrackDto getById(Integer id);
    List<TrackDto> getAll();
    void add(TrackDto trackDto);
    void update(TrackDto trackDto);
    void deleteById(Integer id);
    List<TrackDto> getByTitle(String title);
    List<TrackDto> getByArtist(String artist);
    List<TrackDto> getByAlbum(String album);
    TrackDto getBySpotifyId(String spotifyId);
    TrackDto getByYoutubeId(String youtubeId);
    List<TrackDto> getByUserIdAndState(Integer userId, String state);
    List<TrackDto> getByUserId(Integer userId);
}
