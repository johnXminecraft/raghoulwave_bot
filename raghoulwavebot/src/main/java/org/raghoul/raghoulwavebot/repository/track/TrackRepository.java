package org.raghoul.raghoulwavebot.repository.track;

import org.raghoul.raghoulwavebot.model.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    List<Track> findByTitle(String title);
    List<Track> findByArtist(String artist);
    List<Track> findByAlbum(String album);
    List<Track> findBySpotifyId(String spotifyId);
    List<Track> findByYoutubeId(String youtubeId);
}
