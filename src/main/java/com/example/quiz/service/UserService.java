package com.example.quiz.service;

import com.example.quiz.domain.User;
import com.example.quiz.exception.InvalidParameterException;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.quiz.util.StringUtil.verifyMaxChars;
import static com.example.quiz.util.StringUtil.verifyMinChars;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }
}
