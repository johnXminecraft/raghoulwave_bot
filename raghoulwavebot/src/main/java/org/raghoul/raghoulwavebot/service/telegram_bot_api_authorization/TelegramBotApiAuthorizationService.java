package org.raghoul.raghoulwavebot.service.telegram_bot_api_authorization;

import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramBotApiAuthorizationService {
    String authorize(User user);
}
