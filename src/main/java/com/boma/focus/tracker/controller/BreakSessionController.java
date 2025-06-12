package com.boma.focus.tracker.controller;

import com.boma.focus.tracker.dto.request.CreateBreakSessionRequest;
import com.boma.focus.tracker.dto.request.UpdateBreakSessionRequest;
import com.boma.focus.tracker.dto.response.BreakSessionResponse;
import com.boma.focus.tracker.service.BreakSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/breaks")
@RequiredArgsConstructor
public class BreakSessionController {

    private final BreakSessionService breakSessionService;

    @PostMapping("/start")
    public ResponseEntity<BreakSessionResponse> startBreak(CreateBreakSessionRequest request) {
        return ResponseEntity.ok(breakSessionService.startBreak(request));
    }

    @PostMapping("/end")
    public ResponseEntity<BreakSessionResponse> endBreak(UpdateBreakSessionRequest request) {
        return ResponseEntity.ok(breakSessionService.endBreak(request));
    }

    @GetMapping("/active")
    public ResponseEntity<BreakSessionResponse> getActiveBreakSession() {
        return ResponseEntity.ok(breakSessionService.getActiveBreakSession());
    }

}
