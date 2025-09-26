package org.raghoul.raghoulwavebot.model.bot_track;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bot_tracks")
public class BotTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private String artist;
    @NotNull
    private String album;
    @NotNull
    private String spotifyId;
    @NotNull
    private String youtubeId;
}
