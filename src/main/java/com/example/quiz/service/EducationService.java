package com.example.quiz.service;

import com.example.quiz.domain.Education;
import com.example.quiz.repository.EducationRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

    private final EducationRepository educationRepository = new EducationRepository();

    public void create(long userId, Education education) {
        education.setUserId(userId);
        educationRepository.add(education);
    }
}
