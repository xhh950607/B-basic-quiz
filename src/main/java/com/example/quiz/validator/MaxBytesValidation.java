package com.example.quiz.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxBytesValidation implements ConstraintValidator<MaxBytes, String> {

    private int maxBytes;

    @Override
    public void initialize(MaxBytes constraintAnnotation) {
        maxBytes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value ==null || value.getBytes().length <= maxBytes;
    }
}
