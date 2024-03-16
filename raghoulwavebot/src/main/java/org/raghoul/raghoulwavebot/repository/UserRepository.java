package org.raghoul.raghoulwavebot.repository;

import org.raghoul.raghoulwavebot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByTelegramId(Long telegramId);
}
