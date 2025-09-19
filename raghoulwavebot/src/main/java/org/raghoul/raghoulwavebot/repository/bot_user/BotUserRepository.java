package org.raghoul.raghoulwavebot.repository.bot_user;

import org.raghoul.raghoulwavebot.model.bot_user.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Integer> {
    List<BotUser> findByTelegramId(Long telegramId);
    List<BotUser> findByTag(String tag);
    List<BotUser> findByLanguage(String language);
    List<BotUser> findByState(String state);
}
