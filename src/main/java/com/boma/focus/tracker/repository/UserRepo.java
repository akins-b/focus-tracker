package com.boma.focus.tracker.repository;

import com.boma.focus.tracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

}
