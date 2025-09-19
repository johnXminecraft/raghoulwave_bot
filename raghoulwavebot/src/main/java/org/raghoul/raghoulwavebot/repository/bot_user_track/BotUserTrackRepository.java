package org.raghoul.raghoulwavebot.repository.bot_user_track;

import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.model.bot_user_track.BotUserTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BotUserTrackRepository extends JpaRepository<BotUserTrack, BotUserTrackId> {
    @Query("SELECT but.botTrack FROM BotUserTrack but WHERE but.id.botUserId = :userId AND but.id.state = :state")
    List<BotTrack> findTracksByUserIdAndState(@Param("userId") Integer userId, @Param("state") String state);
    @Query("SELECT but.botTrack FROM BotUserTrack but WHERE but.id.botUserId = :userId")
    List<BotTrack> findTracksByUserId(@Param("userId") Integer userId);
}
