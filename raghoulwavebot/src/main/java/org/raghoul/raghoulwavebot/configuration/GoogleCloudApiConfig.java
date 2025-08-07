package org.raghoul.raghoulwavebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudApiConfig {

    @Value("${raghoulwavebot.config.google.youtube_data_api_v3.api_key}")
    private String apiKey;

    @Bean
    public String apiKey() {
        return apiKey;
    }
}
