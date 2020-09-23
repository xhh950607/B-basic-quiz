package com.example.quiz.domain;

import com.example.quiz.validator.MaxBytes;
import com.example.quiz.validator.MinBytes;
import com.example.quiz.validator.UserGroup;
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

    @NotBlank(message = "用户名不能为空", groups = UserGroup.NameNotBlank.class)
    @MaxBytes(value=128, message="用户名过长", groups = UserGroup.NameMaxBytes.class)
    private String name;

    @Min(value = 17, message = "年龄必须大于16", groups = UserGroup.AgeMin.class)
    private long age;

    @NotBlank(message = "头像图片链接不能为空", groups = UserGroup.AvatarNotBlank.class)
    @MinBytes(value=8, message="头像图片链接过短", groups = UserGroup.AvatarMinBytes.class)
    @MaxBytes(value=512, message="头像图片链接过长", groups = UserGroup.AvatarMaxBytes.class)
    private String avatar;

    @MaxBytes(value=1024, message="个人介绍信息过长", groups = UserGroup.DescriptionMaxBytes.class)
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
