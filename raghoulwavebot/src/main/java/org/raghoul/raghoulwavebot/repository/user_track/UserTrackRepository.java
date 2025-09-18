package org.raghoul.raghoulwavebot.repository.user_track;

import org.raghoul.raghoulwavebot.model.composite_key.user_track.UserTrackId;
import org.raghoul.raghoulwavebot.model.track.Track;
import org.raghoul.raghoulwavebot.model.user_track.UserTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserTrackRepository extends JpaRepository<UserTrack, UserTrackId> {
    @Query("SELECT ut.track FROM UserTrack ut WHERE ut.id.userId = :userId AND ut.id.state = :state")
    List<Track> findTracksByUserIdAndState(@Param("userId") Integer userId, @Param("state") String state);
    @Query("SELECT ut.track FROM UserTrack ut WHERE ut.id.userId = :userId")
    List<Track> findTracksByUserId(@Param("userId") Integer userId);
}
