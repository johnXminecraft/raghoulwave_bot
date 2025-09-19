package org.raghoul.raghoulwavebot.repository.bot_track;

import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BotTrackRepository extends JpaRepository<BotTrack, Integer> {
    List<BotTrack> findByTitle(String title);
    List<BotTrack> findByArtist(String artist);
    List<BotTrack> findByAlbum(String album);
    Optional<BotTrack> findBySpotifyId(String spotifyId);
    Optional<BotTrack> findByYoutubeId(String youtubeId);
}
