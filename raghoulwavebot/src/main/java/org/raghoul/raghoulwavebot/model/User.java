package org.raghoul.raghoulwavebot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "telegram_users")
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
    private String lang;
}
