package com.boma.focus.tracker.repository;

import com.boma.focus.tracker.model.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FocusSessionRepo extends JpaRepository<FocusSession, Long> {
}
