package org.raghoul.raghoulwavebot.service.telegrambot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface MenuService {
    InlineKeyboardMarkup startMenu();
    ReplyKeyboardMarkup getMenu(String botState, String command);
}
