package com.example.quiz.api;

import com.example.quiz.domain.Education;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.service.EducationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.quiz.util.StringUtil.generateStrSpecifiedLength;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EducationController.class)
@AutoConfigureJsonTesters
class EducationControllerTest {

    @MockBean
    EducationService educationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<Education> educationJson;

    @AfterEach
    private void clear(){
        reset(educationService);
    }

    @Nested
    class CreateEducation {

        @Nested
        class RequestIsValid {

            @Test
            void should_call_education_service() throws Exception {
                Education education = new Education(2020L, "title", "description");

                mockMvc.perform(post("/users/1/educations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(educationJson.write(education).getJson()))
                        .andExpect(status().isCreated());

                verify(educationService).create(1L, education);
            }
        }

        @Nested
        class RequestIsInvalid {

            @Nested
            class InvalidUserId {

                @Test
                void should_return_404_and_not_found_user() throws Exception {
                    Education education = new Education(2020L, "title", "description");
                    doThrow(new NotFoundUserException()).when(educationService).create(1L, education);

                    mockMvc.perform(post("/users/1/educations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(educationJson.write(education).getJson()))
                            .andExpect(jsonPath("$.timestamp").isNotEmpty())
                            .andExpect(jsonPath("$.status").value(404))
                            .andExpect(jsonPath("$.error").value("Not Found"))
                            .andExpect(jsonPath("$.message").value("用户不存在"))
                            .andExpect(status().isNotFound());
                }
            }

            @Nested
            class NullYear {

                @Test
                void should_return_400_and_year_cannot_be_blank() throws Exception {
                    Education education = new Education(null, "title", "description");

                    assertCreateEducationFail(education, "年份不能为空");
                }
            }

            @Nested
            class InvalidTitle {

                @Test
                void should_return_400_and_title_cannot_be_blank_given_null_title() throws Exception {
                    Education education = new Education(2020L, null, "description");

                    assertCreateEducationFail(education, "标题不能为空");
                }

                @Test
                void should_return_400_and_title_cannot_be_blank_given_empty_title() throws Exception {
                    Education education = new Education(2020L, "", "description");

                    assertCreateEducationFail(education, "标题不能为空");
                }

                @Test
                void should_return_400_and_title_too_long_given_too_long_title() throws Exception {
                    Education education = new Education(2020L, generateStrSpecifiedLength(257), "description");

                    assertCreateEducationFail(education, "标题过长");
                }
            }

            @Nested
            class InvalidDescription {

                @Test
                void should_return_400_and_title_cannot_be_blank_given_null_description() throws Exception {
                    Education education = new Education(2020L, "title", null);

                    assertCreateEducationFail(education, "描述不能为空");
                }

                @Test
                void should_return_400_and_title_cannot_be_blank_given_empty_description() throws Exception {
                    Education education = new Education(2020L, "title", "");

                    assertCreateEducationFail(education, "描述不能为空");
                }

                @Test
                void should_return_400_and_title_too_long_given_too_long_description() throws Exception {
                    Education education = new Education(2020L, "title", generateStrSpecifiedLength(4097));

                    assertCreateEducationFail(education, "描述过长");
                }
            }
        }
    }

    @Nested
    class GetEducationListByUserId {

        @Nested
        class GivenValidUserId {

            @Test
            void should_return_education_list() throws Exception {
                Education edu1 = new Education(1L, 2020L, "title1", "description1");
                Education edu2 = new Education(1L, 2020L, "title2", "description2");
                List<Education> educations = new ArrayList<>(Arrays.asList(edu1, edu2));
                when(educationService.getList(1L)).thenReturn(educations);

                mockMvc.perform(get("/users/1/educations"))
                        .andExpect(jsonPath("$.length()").value(educations.size()))
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
        }

        @Nested
        class GivenInvalidUserId {

            @Test
            void should_return_404_and_not_found_user() throws Exception {
                when(educationService.getList(1L)).thenThrow(new NotFoundUserException());

                mockMvc.perform(get("/users/1/educations"))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.error").value("Not Found"))
                        .andExpect(jsonPath("$.message").value("用户不存在"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    private void assertCreateEducationFail(Education education, String expectedErrorMsg) throws Exception {
        mockMvc.perform(post("/users/1/educations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(educationJson.write(education).getJson()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(expectedErrorMsg))
                .andExpect(status().isBadRequest());
    }

}