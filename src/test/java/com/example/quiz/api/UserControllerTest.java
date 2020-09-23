package com.example.quiz.api;

import com.example.quiz.domain.User;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.quiz.util.StringUtil.generateStrSpecifiedLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<User> userJson;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class CreateUser {

        @Nested
        class RequestIsValid {

            @Test
            void should_call_user_service() throws Exception {
                User user = new User("张三", 20, "http://pic.17qq.com/img_qqtouxiang/51434034.jpeg");

                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().isCreated());

                verify(userService).createUser(user);
            }
        }

        @Nested
        class ReturnBadRequestGivenInvalidParam {

            @Nested
            class Name {

                @Test
                void is_null() throws Exception {
                    User user = new User(null, 20, "头像链接");

                    assertCreateUserFail(user, "用户名不能为空");
                }

                @Test
                void is_empty_string() throws Exception {
                    User user = new User("", 20, "头像链接");

                    assertCreateUserFail(user, "用户名不能为空");
                }

                @Test
                void too_long() throws Exception {
                    User user = new User(generateStrSpecifiedLength(129), 20, "头像链接");

                    assertCreateUserFail(user, "用户名过长");
                }
            }

            @Nested
            class Age {

                @Test
                void less_than_17() throws Exception {
                    User user = new User("张三", 16, "头像链接");

                    assertCreateUserFail(user, "年龄必须大于16");
                }
            }

            @Nested
            class Avatar {

                @Test
                void is_null() throws Exception {
                    User user = new User("张三", 20, null);

                    assertCreateUserFail(user, "头像图片链接不能为空");
                }

                @Test
                void is_empty_string() throws Exception {
                    User user = new User("张三", 20, "");

                    assertCreateUserFail(user, "头像图片链接不能为空");
                }

                @Test
                void too_short() throws Exception {
                    User user = new User("张三", 20, generateStrSpecifiedLength(7));

                    assertCreateUserFail(user, "头像图片链接过短");
                }

                @Test
                void too_long() throws Exception {
                    User user = new User("张三", 20, generateStrSpecifiedLength(513));

                    assertCreateUserFail(user, "头像图片链接过长");
                }
            }

            @Nested
            class Description {

                @Test
                void too_long() throws Exception {
                    User user = new User("张三", 20, "头像图片链接地址", generateStrSpecifiedLength(1025));

                    assertCreateUserFail(user, "个人介绍信息过长");
                }
            }
        }
    }

    @Nested
    class GetUserInfoById {

        @Test
        void should_return_user_info() throws Exception {
            User user = new User(1L, "Tom", 20, "avator link", "description");
            when(userService.getUserById(1L)).thenReturn(user);

            MockHttpServletResponse response = mockMvc.perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            assertEquals(userJson.write(user).getJson(), response.getContentAsString());
        }

        @Test
        void should_return_404_given_invalid_id() throws Exception {
            when(userService.getUserById(anyLong())).thenThrow(new NotFoundUserException());

            mockMvc.perform(get("/users/1000"))
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