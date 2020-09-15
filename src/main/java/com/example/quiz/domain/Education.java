package com.example.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private Long id;
    private Long userId;
    private long year;
    private String title;
    private String description;
}
