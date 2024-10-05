package org.raghoul.raghoulwavebot.service.telegrambot.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    @Override
    public InlineKeyboardMarkup startMenu(String redirectUriString) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText("Ready");

        inlineKeyboardButton.setCallbackData("Ready");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboardMarkup readyMenu() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow recentTracks = new KeyboardRow();
        KeyboardRow savedTracks = new KeyboardRow();

        recentTracks.add(new KeyboardButton("Recent tracks"));
        savedTracks.add(new KeyboardButton("Saved tracks"));

        keyboard.add(recentTracks);
        keyboard.add(savedTracks);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
