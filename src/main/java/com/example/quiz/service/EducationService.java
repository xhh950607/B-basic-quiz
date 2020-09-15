package com.example.quiz.service;

import com.example.quiz.domain.Education;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.EducationRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

    private final UserRepository userRepository = new UserRepository();
    private final EducationRepository educationRepository = new EducationRepository();

    public void create(long userId, Education education) {
        if (userIsExisted(userId)) {
            education.setUserId(userId);
            educationRepository.add(education);
        } else {
            throw new NotFoundUserException();
        }
    }

    private boolean userIsExisted(long userId) {
        return userRepository.getById(userId) != null;
    }
}
