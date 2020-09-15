package com.example.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    @Min(value = 17, message = "年龄必须大于16")
    private long age;
    @NotBlank(message = "头像图片链接不能为空")
    private String avatar;
    private String description;

    public User(String name, long age, String avatar) {
        this(null, name, age, avatar, null);
    }

    public User(String name, long age, String avatar, String description) {
        this(null, name, age, avatar, description);
    }
}
