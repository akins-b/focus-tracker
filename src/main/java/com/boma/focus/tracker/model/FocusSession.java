package com.boma.focus.tracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class FocusSession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(nullable = false)
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private boolean paused;
    private LocalDateTime pauseStartTime;
    private LocalDateTime pauseEndTime;

}
