package com.boma.focus.tracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BreakSession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "focus_session_id", nullable = false)
    private FocusSession focusSession;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDateTime breakStartTime;

    private LocalDateTime breakEndTime;

    private long duration;

    private boolean isCompleted;
}
