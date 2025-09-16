package org.raghoul.raghoulwavebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RaghoulwavebotApplication {

    /* TODO
    *   Okay I'm rewriting this shit to be actually expandable and therefore usable and pleasing to my perfectionist
    *   mind, so I'm sorry to my future self that I decided to implement these drastic changes. Let's begin:
    *   I need to write many-to-many relation between users and tracks (that's a fucking miracle I still haven't got
    *   a track entity in this cunt), after that I need to rewrite something in SpotifyWebApiService because I want it
    *   to return readable objects and not some fucking disgusting API bullshit objects that have nothing to do
    *   with a perfection of my architecture.
    *   1. DONE: user and track entities
    *   2. many-to-many relation between them
    *   3. humanize spotify service
    *   4. fix other services
    *   5. ???
    *   6. profit
    *  */

    public static void main(String[] args) {
        SpringApplication.run(RaghoulwavebotApplication.class, args);
    }
}
