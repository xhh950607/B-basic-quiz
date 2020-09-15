package com.example.quiz.service;

import com.example.quiz.domain.User;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public void createUser(User user) {
        userRepository.add(user);
    }
}
