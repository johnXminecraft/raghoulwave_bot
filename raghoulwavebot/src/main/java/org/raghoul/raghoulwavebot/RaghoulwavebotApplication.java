package org.raghoul.raghoulwavebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RaghoulwavebotApplication {

    /* TODO
    *   1. figure out what causes excessive youtube quotas
    *   2. create custom exceptions
    *   3. create paging for saved and recent tracks
    *   4. create ability to download these tracks
    *  */

    public static void main(String[] args) {
        SpringApplication.run(RaghoulwavebotApplication.class, args);
    }
}
