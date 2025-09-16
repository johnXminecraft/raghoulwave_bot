package org.raghoul.raghoulwavebot.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
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
}
