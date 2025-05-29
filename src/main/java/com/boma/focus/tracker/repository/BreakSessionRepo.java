package com.boma.focus.tracker.repository;

import com.boma.focus.tracker.model.BreakSession;
import com.boma.focus.tracker.model.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreakSessionRepo extends JpaRepository<BreakSession, Long> {
    Optional<BreakSession> findByBreakSessionIdAndFocusSession(
            Long breakSessionId, FocusSession focusSession);

    Optional<BreakSession> findByUserIdAndIsCompletedFalse(long userId);
}
