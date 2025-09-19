package org.raghoul.raghoulwavebot.model.bot_user;

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
@Table(name = "bot_users")
public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    private Long telegramId;
    @NotNull
    private String tag;
    @NotNull
    private String first;
    private String last;
    @NotNull
    private String language;
    @NotNull
    private String state;
    private String refreshToken;
    @NotNull
    private String botState;
    @NotNull
    private Integer page;
    @OneToMany(mappedBy = "botUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BotUserTrack> botUserTracks;
}
