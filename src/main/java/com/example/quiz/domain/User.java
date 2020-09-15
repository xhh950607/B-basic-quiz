package com.example.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    private long age;
    private String avatar;
    private String description;

    public User(String name, long age, String avatar) {
        this(null, name, age, avatar, null);
    }
}
