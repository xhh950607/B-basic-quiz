package com.example.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private Long id;
    private Long userId;
    @NotNull(message = "年份不能为空")
    private Long year;
    private String title;
    private String description;
}
