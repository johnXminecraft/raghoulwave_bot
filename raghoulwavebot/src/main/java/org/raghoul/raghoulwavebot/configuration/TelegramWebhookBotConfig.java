package org.raghoul.raghoulwavebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TelegramWebhookBotConfig {

    @Value("${raghoulwavebot.config.webhook.url}")
    private String url;
    @Value("${raghoulwavebot.config.webhook.token}")
    private String token;

    private final List<String> allowedUpdatesList = new ArrayList<>(){
        {
            add("callback_query");
            add("message");
        }
    };

    @Bean
    public DefaultBotOptions options() {

        DefaultBotOptions options = new DefaultBotOptions();

        options.setAllowedUpdates(allowedUpdatesList);

        return options;
    }

    @Bean
    public SetWebhook setWebhook() {

        SetWebhook webhook = new SetWebhook();

        webhook.setUrl(url);
        webhook.setAllowedUpdates(allowedUpdatesList);

        return webhook;
    }

    @Bean
    public String botToken() {

        return token;
    }

    @Bean
    public String url() {
        return url;
    }
}
