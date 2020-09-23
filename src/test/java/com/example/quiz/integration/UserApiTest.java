package com.example.quiz.integration;


import com.example.quiz.domain.User;
import com.example.quiz.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.quiz.util.StringUtil.generateStrSpecifiedLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clearDown() {
        userRepository.deleteAll();
    }

    @Nested
    class CreateUser{

        @Test
        void should_create_user() throws Exception {
            User user = new User("Tom", 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

            mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isCreated());

            User storedUser = userRepository.findAll().get(0);
            assertNotNull(storedUser.getId());
            assertEquals(user.getName(), storedUser.getName());
            assertEquals(user.getAge(), storedUser.getAge());
            assertEquals(user.getAvatar(), storedUser.getAvatar());
        }

        @Test
        void should_400_given_name_null() throws Exception {
            User user = new User(null, 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

            assertCreateUserFail(user, "用户名不能为空");
        }

        @Test
        void should_400_given_name_empty() throws Exception {
            User user = new User("", 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

            assertCreateUserFail(user, "用户名不能为空");
        }

        @Test
        void should_400_given_too_long_name() throws Exception {
            User user = new User(generateStrSpecifiedLength(129), 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

            assertCreateUserFail(user, "用户名过长");
        }

        @Test
        void should_400_given_age_less_than_17() throws Exception {
            User user = new User("Tom", 16, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

            assertCreateUserFail(user, "年龄必须大于16");
        }

        @Test
        void should_400_given_avatar_is_null() throws Exception {
            User user = new User("Tom", 20, null);

            assertCreateUserFail(user, "头像图片链接不能为空");
        }

        @Test
        void should_400_given_avatar_is_empty() throws Exception {
            User user = new User("Tom", 20, "");

            assertCreateUserFail(user, "头像图片链接不能为空");
        }

        @Test
        void should_400_given_too_short_avatar() throws Exception {
            User user = new User("Tom", 20, generateStrSpecifiedLength(7));

            assertCreateUserFail(user, "头像图片链接过短");
        }

        @Test
        void should_400_given_too_long_avatar() throws Exception {
            User user = new User("Tom", 20, generateStrSpecifiedLength(513));

            assertCreateUserFail(user, "头像图片链接过长");
        }

        @Test
        void should_400_given_too_long_description() throws Exception {
            User user = new User("Tom", 20, "avator link", generateStrSpecifiedLength(1025));

            assertCreateUserFail(user, "个人介绍信息过长");
        }
    }

    @Nested
    class GetUserInfo{

        @Test
        void should_return_user_info_given_valid_id() throws Exception {
            User user = new User("Tom", 20, "avator link", "description");
            long id = userRepository.save(user).getId();

            mockMvc.perform(get("/users/" + id))
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value(user.getName()))
                    .andExpect(jsonPath("$.age").value(user.getAge()))
                    .andExpect(jsonPath("$.avatar").value(user.getAvatar()))
                    .andExpect(jsonPath("$.description").value(user.getDescription()))
                    .andExpect(status().isOk());
        }

        @Test
        void should_404_given_invalid_id() throws Exception {
            mockMvc.perform(get("/users/100000"))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("用户不存在"))
                    .andExpect(status().isNotFound());
        }
    }



    private void assertCreateUserFail(User user, String expectedErrorMsg) throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(expectedErrorMsg));
    }

}