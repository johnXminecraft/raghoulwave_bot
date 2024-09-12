package org.raghoul.raghoulwavebot.repository;

import org.raghoul.raghoulwavebot.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByTelegramId(Long telegramId);
    List<User> findByState(String state);
}
