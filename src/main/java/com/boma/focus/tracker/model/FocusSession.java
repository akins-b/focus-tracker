package com.boma.focus.tracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FocusSession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    private LocalDateTime endTime;

    private boolean onBreak = false;

}
