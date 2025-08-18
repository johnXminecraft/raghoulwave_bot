package org.raghoul.raghoulwavebot.service.handleupdate;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface HandleUpdateService {
    SendMessage handleUpdate(Update update);
}
