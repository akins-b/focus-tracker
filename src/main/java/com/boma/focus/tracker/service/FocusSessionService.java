package com.boma.focus.tracker.service;

import com.boma.focus.tracker.dto.request.CreateFocusSessionRequest;
import com.boma.focus.tracker.dto.request.QueryFocusSession;
import com.boma.focus.tracker.dto.response.FocusSessionResponse;
import com.boma.focus.tracker.model.FocusSession;
import com.boma.focus.tracker.repository.FocusSessionRepo;
import com.boma.focus.tracker.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FocusSessionService {
    private final FocusSessionRepo focusSessionRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public FocusSessionResponse startFocusSession(CreateFocusSessionRequest request) {
        long userId = userService.getUserId();
        FocusSession newFocusSession = new FocusSession();
        newFocusSession.setStartTime(request.getStartTime());
        newFocusSession.setPaused(request.isPaused());
        newFocusSession.setUser(userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        focusSessionRepo.save(newFocusSession);
        return modelMapper.map(newFocusSession, FocusSessionResponse.class);
    }

    public FocusSessionResponse endFocusSession(long focusSessionId) {
        long userId = userService.getUserId();
        FocusSession focusSession = focusSessionRepo.findById(focusSessionId)
                .orElseThrow(()-> new IllegalArgumentException("Focus session not found"));

        if (focusSession.getUser() != null && focusSession.getUser().getId() == userId) {
            LocalDateTime endTime = LocalDateTime.now();
            focusSession.setEndTime(endTime);

            long durationBeforeBreak;
            long durationAfterBreak = 0;

            if (focusSession.getPauseStartTime() != null) {
                durationBeforeBreak = Duration.between(focusSession.getStartTime(), focusSession.getPauseStartTime()).toSeconds();
            } else {
                durationBeforeBreak = Duration.between(focusSession.getStartTime(), endTime).toSeconds();
            }

            if (focusSession.getPauseEndTime() != null) {
                durationAfterBreak = Duration.between(focusSession.getPauseEndTime(), endTime).toSeconds();
            }
            long duration = durationBeforeBreak + durationAfterBreak;
            focusSession.setDuration(duration);

            focusSessionRepo.save(focusSession);
            return modelMapper.map(focusSession, FocusSessionResponse.class);
        }
        throw new IllegalArgumentException("Current user can't edit this focus session");
    }

    public Page<FocusSessionResponse> getAllSessions(QueryFocusSession request) {
        long userId = userService.getUserId();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<FocusSession> focusSessions = focusSessionRepo.findAllByUserId(userId, pageable);

        if (focusSessions.isEmpty()) {
            return Page.empty();
        }
        return focusSessions.map(focusSession -> {
            FocusSessionResponse response = modelMapper.map(focusSession, FocusSessionResponse.class);
            return response;
        });

    }

    public FocusSessionResponse getActiveSession() {
        long userId = userService.getUserId();
        FocusSession focusSession = focusSessionRepo.findByUserIdAndPausedFalseAndEndTimeIsNull(userId)
                .orElseThrow(()-> new IllegalArgumentException("Active focus session not found"));

        return modelMapper.map(focusSession, FocusSessionResponse.class);
    }

    public Void pauseSession(long focusSessionId) {
        long userId = userService.getUserId();

        FocusSession focusSession = focusSessionRepo.findById(focusSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Focus session not found"));

        if (focusSession.getUser() != null && focusSession.getUser().getId() == userId) {
            if (focusSession.isPaused()) {
                throw new IllegalStateException("Focus session is already paused");
            }
            focusSession.setPaused(true);
            focusSession.setPauseStartTime(LocalDateTime.now());
            focusSessionRepo.save(focusSession);
        }
        throw new IllegalArgumentException("Current user can't pause this focus session");

    }

    public void resumeSession(long focusSessionId) {
        long userId = userService.getUserId();

        FocusSession focusSession = focusSessionRepo.findById(focusSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Focus session not found"));

        if (focusSession.getUser() != null && focusSession.getUser().getId() == userId) {
            if (focusSession.isPaused()) {
                focusSession.setPaused(false);
                focusSession.setPauseEndTime(LocalDateTime.now());
                focusSessionRepo.save(focusSession);
            }
            throw new IllegalStateException("Focus session is already on break");

        }
        throw new IllegalArgumentException("Current user can't pause this focus session");
    }
}
