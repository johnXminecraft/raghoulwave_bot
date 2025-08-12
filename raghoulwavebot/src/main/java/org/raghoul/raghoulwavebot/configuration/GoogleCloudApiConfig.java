package org.raghoul.raghoulwavebot.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleCloudApiConfig {

    @Value("${raghoulwavebot.config.google.youtube_data_api_v3.api_key}")
    private String youtubeApiKey;

    @Bean
    public String youtubeApiKey() {
        return youtubeApiKey;
    }

    @Bean
    public YouTube youTube() {
        try {
            return new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    request -> {
                    }).setApplicationName("link-receiver")
              .setYouTubeRequestInitializer(new YouTubeRequestInitializer(youtubeApiKey))
              .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Failed to create YouTube client", e);
        }
    }
}
