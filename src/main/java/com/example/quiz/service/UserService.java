package com.example.quiz.service;

import com.example.quiz.domain.User;
import com.example.quiz.exception.InvalidParameterException;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository = new UserRepository();

    private static final int NAME_MAX_CHARS = 128;
    private static final int AVATAR_MIN_CHARS = 8;
    private static final int AVATAR_MAX_CHARS = 512;

    public void createUser(User user) {
        validateName(user.getName());
        validateAvatar(user.getAvatar());
        userRepository.add(user);
    }

    private void validateName(String name) {
        if(!verifyMaxChars(name, NAME_MAX_CHARS))
            throw new InvalidParameterException("用户名过长");
    }

    private void validateAvatar(String avatar) {
        if(!verifyMinChars(avatar, AVATAR_MIN_CHARS))
            throw new InvalidParameterException("头像图片链接过短");
        if(!verifyMaxChars(avatar, AVATAR_MAX_CHARS))
            throw new InvalidParameterException("头像图片链接过长");
    }

    private boolean verifyMaxChars(String str, int maxChars) {
        return str.getBytes().length <= maxChars;
    }

    private boolean verifyMinChars(String str, int minChars) {
        return str.getBytes().length >= minChars;
    }
}
