package com.boma.focus.tracker.service;

import com.boma.focus.tracker.model.Users;
import com.boma.focus.tracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(Users user) {
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        return "failed";
    }

    public long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null && authentication.isAuthenticated()) {
            Users user = userRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return user.getId();
        }
        throw new IllegalStateException("User is not authenticated");
    }
}
