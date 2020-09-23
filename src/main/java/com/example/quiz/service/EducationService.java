package com.example.quiz.service;

import com.example.quiz.domain.Education;
import com.example.quiz.domain.User;
import com.example.quiz.exception.InvalidParameterException;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.EducationRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.quiz.util.StringUtil.verifyMaxChars;

@Service
public class EducationService {

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;

    public EducationService(UserRepository userRepository, EducationRepository educationRepository) {
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
    }

    public void create(long userId, Education education) {
        User user = findUserOrThrowException(userId);

        education.setUserId(user.getId());
        educationRepository.save(education);
    }

    public List<Education> getList(long userId) {
        User user = findUserOrThrowException(userId);
        return educationRepository.findAllByUserId(user.getId());
    }

    private User findUserOrThrowException(long userId){
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
