package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "log")
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private LocalDateTime logDatetime;

    public Log(String action, String message, boolean success) {
        this.action = action;
        this.message = message;
        this.success = success;

        this.logDatetime = LocalDateTime.now();
    }
}
