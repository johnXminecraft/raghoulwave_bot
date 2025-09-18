package org.raghoul.raghoulwavebot.model.track;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.raghoul.raghoulwavebot.model.user_track.UserTrack;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tracks")
public class Track {
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
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserTrack> userTracks;
}
