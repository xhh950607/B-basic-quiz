package com.example.quiz.api;

import com.example.quiz.domain.Education;
import com.example.quiz.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EducationController {

    @Autowired
    private EducationService educationService;

    @PostMapping("/users/{userId}/educations")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable long userId, @RequestBody @Valid Education education) {
        educationService.create(userId, education);
    }
}
