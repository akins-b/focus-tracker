package com.boma.focus.tracker.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FocusSessionResponse {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private boolean onBreak;
}
