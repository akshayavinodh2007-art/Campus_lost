package com.campusfind.campusfind.service;

import com.campusfind.campusfind.dto.LoginRequest;
import com.campusfind.campusfind.dto.RegisterRequest;
import com.campusfind.campusfind.entity.User;
import com.campusfind.campusfind.exception.BusinessRuleViolationException;
import com.campusfind.campusfind.exception.ResourceNotFoundException;
import com.campusfind.campusfind.exception.UnauthorizedException;
import com.campusfind.campusfind.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleViolationException("Username '" + request.getUsername() + "' is already registered");
        }
        User user = new User(request.getUsername(), request.getPassword(), request.getRole());
        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
