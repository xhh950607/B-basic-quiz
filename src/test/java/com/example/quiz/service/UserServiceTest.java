package com.example.quiz.service;

import com.example.quiz.domain.User;
import com.example.quiz.exception.NotFoundUserException;
import com.example.quiz.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUser {

        @Test
        void should_call_user_repository() {
            User user = new User("Tom", 20L, "avator link");

            userService.createUser(user);

            verify(userRepository).save(user);
        }
    }

    @Nested
    class GetUserById {

        @Nested
        class GivenInvalidUserId {

            @Test
            void should_throw_not_found_user_exception() {
                when(userRepository.findById(1L)).thenThrow(new NotFoundUserException());

                assertThrows(NotFoundUserException.class, () -> userService.getUserById(1));
            }
        }

        @Nested
        class GivenValidUserId {

            @Test
            void should_return_user() {
                User user = new User(1L, "Tom", 20L, "avator link", null);

                when(userRepository.findById(1L))
                        .thenReturn(Optional.of(user));

                assertEquals(user, userService.getUserById(1L));
            }
        }
    }

}