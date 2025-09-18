package org.raghoul.raghoulwavebot.service.telegrambot;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raghoul.raghoulwavebot.service.handle_update.HandleUpdateServiceImpl;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class HandleUpdateServiceImplTest {

    /* TODO
    *   rewrite completely*/

    private final Long telegramId = 132281488L;

    @InjectMocks
    private HandleUpdateServiceImpl testInstance;

    @Mock
    private Update update;
    @Mock
    private BotApiMethod<?> method;
    @Mock
    private Message incomingMessage;
    @Mock
    private User user;

    /*@Test
    public void shouldHandleUpdateAndSendMessage() {

        when(update.getMessage()).thenReturn(incomingMessage);
        when(update.getMessage().getChatId()).thenReturn(telegramId);

        method = testInstance.handleUpdate(update);

        assertEquals(Long.toString(update.getMessage().getChatId()), method.getMethod());
    }*/
}