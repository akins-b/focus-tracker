package com.boma.focus.tracker.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateBreakSessionRequest {
    long focusSessionId;
    long breakSessionId;
    LocalDateTime breakEndTime;
}
