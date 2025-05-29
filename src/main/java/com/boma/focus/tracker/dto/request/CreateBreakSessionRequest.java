package com.boma.focus.tracker.dto.request;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class CreateBreakSessionRequest {
    long focusSessionId;
    LocalDateTime breakStartTime;
}
