package org.raghoul.raghoulwavebot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TelegramWebhookBotConfig {

    @Bean
    public DefaultBotOptions options() {

        List<String> allowedUpdatesList = new ArrayList<>();
        allowedUpdatesList.add("callback_query");
        allowedUpdatesList.add("message");

        DefaultBotOptions options = new DefaultBotOptions();
        options.setAllowedUpdates(allowedUpdatesList);

        return options;
    }

    @Bean
    public SetWebhook setWebhook() {

        List<String> allowedUpdatesList = new ArrayList<>();
        allowedUpdatesList.add("callback_query");
        allowedUpdatesList.add("message");

        SetWebhook webhook = new SetWebhook();
        webhook.setUrl("https://677d-109-122-6-227.ngrok-free.app");
        webhook.setAllowedUpdates(allowedUpdatesList);

        return webhook;
    }

    @Bean
    public String botToken() {

        return "6954126928:AAHyjjbIne_5p86iDC49X0hS8LphTniwB-k";
    }
}
