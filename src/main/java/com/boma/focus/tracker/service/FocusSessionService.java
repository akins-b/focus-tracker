package com.boma.focus.tracker.service;

import com.boma.focus.tracker.model.FocusSession;
import com.boma.focus.tracker.repository.FocusSessionRepo;
import org.springframework.stereotype.Service;

@Service
public class FocusSessionService {
    private final FocusSessionRepo focusSessionRepo;

    public FocusSessionService(FocusSessionRepo focusSessionRepo) {
        this.focusSessionRepo = focusSessionRepo;
    }

    public FocusSession startFocusSession() {
        FocusSession newFocusSession = new FocusSession();
        return focusSessionRepo.save(newFocusSession);
    }
}
