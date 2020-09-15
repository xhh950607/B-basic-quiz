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

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.quiz.util.StringUtil.generateStrSpecifiedLength;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        Education education = new Education(null, null, 2020L, "title", "description");

        mockMvc.perform(post("/users/" + user.getId() + "/educations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(education)))
                .andExpect(status().isCreated());

        Education stored = educationRepository.getAllByUserId(user.getId()).get(0);
        assertEquals(education.getYear(), stored.getYear());
        assertEquals(education.getTitle(), stored.getTitle());
        assertEquals(education.getDescription(), stored.getDescription());
    }

    @Test
    void should_not_found_user_when_create_education_given_invalid_userId() throws Exception {
        Education education = new Education(null, null, 2020L, "title", "description");

        mockMvc.perform(post("/users/100000/educations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(education)))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("用户不存在"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_400_when_create_education_given_null_year() throws Exception {
        Education education = new Education(null, null, null, "title", "description");

        assertCreateEducationFail(education, "年份不能为空");
    }

    @Test
    void should_400_when_create_education_given_null_title() throws Exception {
        Education education = new Education(null, null, 2020L, null, "description");

        assertCreateEducationFail(education, "标题不能为空");
    }

    @Test
    void should_400_when_create_education_given_empty_title() throws Exception {
        Education education = new Education(null, null, 2020L, "", "description");

        assertCreateEducationFail(education, "标题不能为空");
    }

    @Test
    void should_400_when_create_education_given_too_long_title() throws Exception {
        Education education = new Education(null, null, 2020L, generateStrSpecifiedLength(257), "description");

        assertCreateEducationFail(education, "标题过长");
    }

    @Test
    void should_400_when_create_education_given_null_description() throws Exception {
        Education education = new Education(null, null, 2020L, "title", null);

        assertCreateEducationFail(education, "描述不能为空");
    }

    @Test
    void should_400_when_create_education_given_empty_description() throws Exception {
        Education education = new Education(null, null, 2020L, "title", "");

        assertCreateEducationFail(education, "描述不能为空");
    }

    @Test
    void should_400_when_create_education_given_too_long_description() throws Exception {
        Education education = new Education(null, null, 2020L, "title", generateStrSpecifiedLength(4097));

        assertCreateEducationFail(education, "描述过长");
    }

    @Test
    void should_get_education_list_by_id() throws Exception {
        Education edu1 = new Education(null, user.getId(), 2020L, "title1", "description1");
        Education edu2 = new Education(null, user.getId(), 2020L, "title2", "description2");
        educationRepository.add(edu1);
        educationRepository.add(edu2);

        long userId_2 = userRepository.add(new User("张三", 20, "头像链接"));
        educationRepository.add(new Education(null, userId_2, 2020L, "title3", "description3"));

        mockMvc.perform(get("/users/" + user.getId() + "/educations"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(edu1.getUserId()))
                .andExpect(jsonPath("$[0].year").value(edu1.getYear()))
                .andExpect(jsonPath("$[0].title").value(edu1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(edu1.getDescription()))
                .andExpect(jsonPath("$[1].userId").value(edu2.getUserId()))
                .andExpect(jsonPath("$[1].year").value(edu2.getYear()))
                .andExpect(jsonPath("$[1].title").value(edu2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(edu2.getDescription()))
                .andExpect(status().isOk());
    }

    private void assertCreateEducationFail(Education education, String expectedErrorMsg) throws Exception {
        mockMvc.perform(post("/users/" + user.getId() + "/educations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(education)))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(expectedErrorMsg))
                .andExpect(status().isBadRequest());
    }

}