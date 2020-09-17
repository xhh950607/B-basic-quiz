package com.example.quiz.service;

import com.example.quiz.domain.Education;
import com.example.quiz.exception.InvalidParameterException;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.EducationRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.quiz.util.StringUtil.verifyMaxChars;

@Service
public class EducationService {

    private final UserRepository userRepository = new UserRepository();
    private final EducationRepository educationRepository = new EducationRepository();

    private static final int TITLE_MAX_CHARS = 256;
    private static final int DESCRIPTION_MAX_CHARS = 4096;

    public void create(long userId, Education education) {
        // GTB: - 下面这种请，可以先抛异常，比如：
//        if (!userIsExisted(userId)) {
//            throw new NotFoundUserException();
//        }
//        validateTitle(education.getTitle());
//        validateDescription(education.getDescription());
//        education.setUserId(userId);
//        educationRepository.add(education);

        if (userIsExisted(userId)) {
            validateTitle(education.getTitle());
            validateDescription(education.getDescription());
            education.setUserId(userId);
            educationRepository.add(education);
        } else {
            throw new NotFoundUserException();
        }
    }

    public List<Education> getList(long userId) {
        if(!userIsExisted(userId))
            throw new NotFoundUserException();
        return educationRepository.getAllByUserId(userId);
    }

    private boolean userIsExisted(long userId) {
        return userRepository.getById(userId) != null;
    }

    private void validateTitle(String title){
        if(!verifyMaxChars(title, TITLE_MAX_CHARS))
            throw new InvalidParameterException("标题过长");
    }

    private void validateDescription(String description){
        if(!verifyMaxChars(description, DESCRIPTION_MAX_CHARS))
            throw new InvalidParameterException("描述过长");
    }
}
