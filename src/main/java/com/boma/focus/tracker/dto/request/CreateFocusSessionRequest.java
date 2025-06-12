package com.boma.focus.tracker.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateFocusSessionRequest {
    long userId;
    LocalDateTime startTime;
}
