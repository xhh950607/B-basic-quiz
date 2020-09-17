package com.example.quiz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

// GTB: - ErrorResponse所在的包不太合适
@Data
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.timestamp = Instant.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }
}
