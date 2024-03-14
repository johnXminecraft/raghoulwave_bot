package org.raghoul.raghoulwavebot.service.telegrambot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class TelegramBotServiceImplTest {

    private final Long telegramId = 132281488L;

    @InjectMocks
    private TelegramBotServiceImpl testInstance;

    @Mock
    private Update update;
    @Mock
    private SendMessage message;
    @Mock
    private Message incomingMessage;

    @Test
    public void shouldHandleUpdateAndSendMessage() {

        when(update.getMessage()).thenReturn(incomingMessage);
        when(update.getMessage().getChatId()).thenReturn(telegramId);

        message = testInstance.handleUpdate(update);

        assertEquals(Long.toString(update.getMessage().getChatId()), message.getChatId());
    }
}