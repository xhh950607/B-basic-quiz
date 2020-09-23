package com.example.quiz.service;

import com.example.quiz.domain.Education;
import com.example.quiz.domain.User;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.EducationRepository;
import com.example.quiz.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

    @Mock
    private EducationRepository educationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EducationService educationService;

    @AfterEach
    private void clear() {
        reset(educationRepository);
        reset(userRepository);
    }

    @Nested
    class CreateEducation {

        @Test
        void should_call_education_respository() {
            User user = new User(1L, "Tom", 20L, "avator link", "description");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            Education education = new Education(2020L, "title", "description");
            educationService.create(1, education);

            education.setUserId(user.getId());
            verify(educationRepository).save(education);
        }
    }

    @Nested
    class GetEducationListByUserId {

        @Nested
        class GivenValidUserId {

            @Test
            void should_return_education_list() {
                User user = new User(1L, "Tom", 20L, "avator link", "description");
                when(userRepository.findById(1L)).thenReturn(Optional.of(user));

                List<Education> educations = Arrays.asList(
                        new Education(1L, 1L, 2020L, "title", "description", null),
                        new Education(2L, 1L, 2020L, "title2", "description2", null)
                );
                when(educationRepository.findAllByUserId(1L)).thenReturn(educations);

                assertEquals(educations, educationService.getList(1L));
            }
        }

        @Nested
        class GivenInvalidUserId {

            @Test
            void should_throw_not_found_user_exception() {
                when(userRepository.findById(1L)).thenThrow(new NotFoundUserException());
                when(educationRepository.findAllByUserId(1L)).thenReturn(new ArrayList<>());

                assertThrows(NotFoundUserException.class, () -> educationService.getList(1L));
            }
        }
    }
}