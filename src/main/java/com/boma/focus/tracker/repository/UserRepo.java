package com.boma.focus.tracker.repository;

import com.boma.focus.tracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

}
