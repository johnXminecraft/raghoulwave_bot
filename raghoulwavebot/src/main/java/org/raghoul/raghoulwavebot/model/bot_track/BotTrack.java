package org.raghoul.raghoulwavebot.model.bot_track;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.raghoul.raghoulwavebot.model.bot_user_track.BotUserTrack;

import java.util.Set;

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
    @OneToMany(mappedBy = "botTrack", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BotUserTrack> botUserTracks;
}
