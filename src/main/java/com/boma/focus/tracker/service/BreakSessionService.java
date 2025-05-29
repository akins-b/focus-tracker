package com.boma.focus.tracker.service;

import com.boma.focus.tracker.dto.request.CreateBreakSessionRequest;
import com.boma.focus.tracker.dto.request.UpdateBreakSessionRequest;
import com.boma.focus.tracker.dto.response.BreakSessionResponse;
import com.boma.focus.tracker.model.BreakSession;
import com.boma.focus.tracker.model.FocusSession;
import com.boma.focus.tracker.repository.BreakSessionRepo;
import com.boma.focus.tracker.repository.FocusSessionRepo;
import com.boma.focus.tracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BreakSessionService {
    private final BreakSessionRepo breakSessionRepo;
    private final FocusSessionRepo focusSessionRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public BreakSessionResponse startBreak(CreateBreakSessionRequest request) {
        long userId = userService.getUserId();
        FocusSession focusSession = focusSessionRepo.findById(request.getFocusSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Focus session not found"));

        if (focusSession.getUser() == null || focusSession.getUser().getId() != userId) {
            throw new IllegalArgumentException("Current user can't edit this focus session");
        }

        if (focusSession.isOnBreak()) {
            throw new IllegalStateException("Focus session is already on break");
        }
        focusSession.setOnBreak(true);
        focusSessionRepo.save(focusSession);

        BreakSession breakSession = new BreakSession();
        breakSession.setFocusSession(focusSession);
        breakSession.setUser(userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        breakSession.setBreakStartTime(request.getBreakStartTime());

        breakSessionRepo.save(breakSession);
        return modelMapper.map(breakSession, BreakSessionResponse.class);
    }

    public BreakSessionResponse endBreak(UpdateBreakSessionRequest request) {
        long userId = userService.getUserId();
        FocusSession focusSession = focusSessionRepo.findById(request.getFocusSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Focus session not found"));

        BreakSession breakSession = breakSessionRepo.findByBreakSessionIdAndFocusSession(
                request.getBreakSessionId(), focusSession)
                .orElseThrow(() -> new IllegalArgumentException("Break session not found"));

        if (breakSession.getUser() != null || breakSession.getUser().getId() == userId) {
            breakSession.setCompleted(true);
            breakSession.setBreakEndTime(request.getBreakEndTime());

            long duration = Duration.between(breakSession.getBreakStartTime(), request.getBreakEndTime()).toSeconds();
            breakSession.setDuration(duration);

            breakSessionRepo.save(breakSession);

            focusSession.setOnBreak(false);
            focusSessionRepo.save(focusSession);
            return modelMapper.map(breakSession, BreakSessionResponse.class);
        }
        else {
            throw new IllegalArgumentException("Current user can't edit this break session");
        }
    }

    public BreakSessionResponse getActiveBreakSession() {
        long userId = userService.getUserId();
        BreakSession breakSession = breakSessionRepo.findByUserIdAndIsCompletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("Active break session not found"));

        return modelMapper.map(breakSession, BreakSessionResponse.class);
    }
}
