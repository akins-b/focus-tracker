package com.boma.focus.tracker.controller;

import com.boma.focus.tracker.dto.request.CreateFocusSessionRequest;
import com.boma.focus.tracker.dto.request.QueryFocusSession;
import com.boma.focus.tracker.dto.response.FocusSessionResponse;
import com.boma.focus.tracker.service.FocusSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class FocusSessionController {
    private final FocusSessionService focusSessionService;

    @PostMapping("/start")
    public ResponseEntity<FocusSessionResponse> startFocusSession(@RequestBody CreateFocusSessionRequest request) {
        return ResponseEntity.ok(focusSessionService.startFocusSession(request));
    }

    @PostMapping("/end")
    public ResponseEntity<FocusSessionResponse> endFocusSession(@RequestParam long focusSessionId) {
        return ResponseEntity.ok(focusSessionService.endFocusSession(focusSessionId));
    }

    @PostMapping("/pause")
    public ResponseEntity<Void> pauseFocusSession(@RequestParam long focusSessionId) {
        focusSessionService.pauseSession(focusSessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resume")
    public ResponseEntity<Void> resumeFocusSession(@RequestParam long focusSessionId){
        focusSessionService.resumeSession(focusSessionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<FocusSessionResponse>> getAllFocusSessions(QueryFocusSession request) {
        return ResponseEntity.ok(focusSessionService.getAllSessions(request));
    }

    @GetMapping("/active")
    public ResponseEntity<FocusSessionResponse> getActiveFocusSession() {
        return ResponseEntity.ok(focusSessionService.getActiveSession());
    }
}
