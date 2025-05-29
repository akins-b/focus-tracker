package com.boma.focus.tracker.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BreakSessionResponse {
    private long id;
    private long focusSessionId;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;
    private long duration;
    private boolean isCompleted;
}
