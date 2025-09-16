package org.raghoul.raghoulwavebot.repository.user;

import org.raghoul.raghoulwavebot.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByTelegramId(Long telegramId);
    List<User> findByTag(String tag);
    List<User> findByLanguage(String language);
    List<User> findByState(String state);
}
