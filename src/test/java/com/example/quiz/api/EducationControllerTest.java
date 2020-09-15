package com.example.quiz.api;

import com.example.quiz.domain.Education;
import com.example.quiz.domain.User;
import com.example.quiz.repository.EducationRepository;
import com.example.quiz.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EducationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final UserRepository userRepository = new UserRepository();
    private final EducationRepository educationRepository = new EducationRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("张三", 20, "头像链接");
        long id = userRepository.add(user);
        user.setId(id);
    }

    @AfterEach
    void clearDown() {
        educationRepository.clear();
        userRepository.clear();
    }

    @Test
    void should_create_education() throws Exception {
        Education education = new Education(null, null, 2020, "title", "description");

        mockMvc.perform(post("/users/" + user.getId() + "/educations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(education)))
                .andExpect(status().isCreated());

        Education stored = educationRepository.getAllByUserId(user.getId()).get(0);
        assertEquals(education.getYear(), stored.getYear());
        assertEquals(education.getTitle(), stored.getTitle());
        assertEquals(education.getDescription(), stored.getDescription());
    }

}