package com.example.quiz.repository;

import com.example.quiz.domain.Education;
import com.example.quiz.domain.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EducationRepositoryTest {

    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private UserRepository userRepository;

    @Nested
    class FindAllByUserId {

        @Nested
        class GivenExistedUserId {

            @Test
            void should_return_educations() {
                User user1 = userRepository.save(new User("Tom",20L, "avator link"));
                User user2 = userRepository.save(new User("Bob",20L, "avator link"));
                Education edu1 = educationRepository.save(
                        new Education(user1.getId(), 2001L, "title1", "description1"));
                Education edu2 = educationRepository.save(
                        new Education(user1.getId(), 2002L, "title2", "description2"));
                Education edu3 = educationRepository.save(
                        new Education(user2.getId(), 2003L, "title3", "description3"));

                List<Education> educations = educationRepository.findAllByUserId(user1.getId());

                assertEquals(2, educations.size());
                assertEquals(edu1.getTitle(), educations.get(0).getTitle());
                assertEquals(edu2.getTitle(), educations.get(1).getTitle());
            }
        }
    }
}