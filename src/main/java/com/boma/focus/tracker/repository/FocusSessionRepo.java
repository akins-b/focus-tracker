package com.boma.focus.tracker.repository;

import com.boma.focus.tracker.model.FocusSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FocusSessionRepo extends JpaRepository<FocusSession, Long> {
    Optional<FocusSession> findById(Long id);
    Page<FocusSession> findAllByUserId(long userId, Pageable pageable);
    Optional<FocusSession> findByUserIdAndOnBreakFalseAndEndTimeIsNull(long userId);
}
