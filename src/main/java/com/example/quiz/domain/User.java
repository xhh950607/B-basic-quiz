package com.example.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String name;
    @Min(value = 17, message = "年龄必须大于16")
    private long age;
    @NotBlank(message = "头像图片链接不能为空")
    private String avatar;
    private String description;

    public User(String name, long age, String avatar) {
        this.name = name;
        this.age = age;
        this.avatar = avatar;
    }

    public User(String name, long age, String avatar, String description) {
        this.name = name;
        this.age = age;
        this.avatar = avatar;
        this.description = description;
    }
}
