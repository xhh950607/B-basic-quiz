package com.example.quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Education {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "年份不能为空")
    private Long year;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "描述不能为空")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public Education(Long year, String title, String description) {
        this.year = year;
        this.title = title;
        this.description = description;
    }

    public Education(Long userId, Long year, String title, String description) {
        this.userId = userId;
        this.year = year;
        this.title = title;
        this.description = description;
    }
}
