package com.example.quiz.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinBytesValidation implements ConstraintValidator<MinBytes, String> {

    private int minBytes;

    @Override
    public void initialize(MinBytes constraintAnnotation) {
        minBytes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.getBytes().length >= minBytes;
    }
}
