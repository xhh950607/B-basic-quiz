package com.example.quiz.api;

import com.example.quiz.domain.User;
import com.example.quiz.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final UserRepository userRepository = new UserRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clearDown(){
        userRepository.clear();
    }

    @Test
    void should_create_user() throws Exception {
        User user = new User("张三", 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        User storedUser = userRepository.getAll().get(0);
        assertNotNull(storedUser.getId());
        assertEquals(user.getName(),storedUser.getName());
        assertEquals(user.getAge(),storedUser.getAge());
        assertEquals(user.getAvatar(),storedUser.getAvatar());
    }

    @Test
    void should_400_when_create_user_given_name_null() throws Exception {
        User user = new User(null, 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

        assertCreateUserFail(user, "用户名不能为空");
    }

    @Test
    void should_400_when_create_user_given_name_empty() throws Exception {
        User user = new User("", 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

        assertCreateUserFail(user, "用户名不能为空");
    }

    @Test
    void should_400_when_create_user_given_too_long_name() throws Exception {
        User user = new User("一个超级长的名字一个超级长的名字一个超级长的名字一个超级长的名字一个超级长的名字一个超级长的名字",
                20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

        assertCreateUserFail(user, "用户名过长");
    }

    @Test
    void should_400_when_create_user_given_age_less_than_17() throws Exception {
        User user = new User("张三", 16, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

        assertCreateUserFail(user, "年龄必须大于16");
    }

    @Test
    void should_400_when_create_user_given_avatar_is_null() throws Exception {
        User user = new User("张三", 20, null);

        assertCreateUserFail(user, "头像图片链接不能为空");
    }

    @Test
    void should_400_when_create_user_given_avatar_is_empty() throws Exception {
        User user = new User("张三", 20, "");

        assertCreateUserFail(user, "头像图片链接不能为空");
    }

    @Test
    void should_400_when_create_user_given_too_short_avatar() throws Exception {
        User user = new User("张三", 20, "1234567");

        assertCreateUserFail(user, "头像图片链接过短");
    }

    @Test
    void should_400_when_create_user_given_too_long_avatar() throws Exception {
        User user = new User("张三", 20,
                "头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址头像图片链接地址");

        assertCreateUserFail(user, "头像图片链接过长");
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