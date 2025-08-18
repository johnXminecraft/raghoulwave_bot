package org.raghoul.raghoulwavebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadTrackConfig {

    @Value("${raghoulwavebot.config.ytdlp.download-dir}")
    private String downloadPath;

    @Bean
    public String downloadPath() {
        return downloadPath;
    }
}
