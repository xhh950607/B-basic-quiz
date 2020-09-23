package com.example.quiz.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = MinBytesValidation.class)
@Target({ METHOD, FIELD})
@Retention(RUNTIME)
public @interface MinBytes {
    int value();

    String message() default "{javax.validation.constraints.MaxBytes.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
