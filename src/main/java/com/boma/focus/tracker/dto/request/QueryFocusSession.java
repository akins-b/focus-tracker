package com.boma.focus.tracker.dto.request;

import lombok.Data;

@Data
public class QueryFocusSession {
    private int page = 1;
    private int size = 25;
}
