package org.raghoul.raghoulwavebot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private Long telegramId;
    private String tag;
    private String first;
    private String last;
    private String lang;
}
