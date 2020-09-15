package com.example.quiz.api;

import com.example.quiz.domain.Education;
import com.example.quiz.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class EducationController {

    @Autowired
    private EducationService educationService;

    @PostMapping("/users/{userId}/educations")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable long userId, @RequestBody @Valid Education education) {
        educationService.create(userId, education);
    }

    @GetMapping("/users/{userId}/educations")
    public List<Education> getList(@PathVariable long userId){
        return educationService.getList(userId);
    }
}
