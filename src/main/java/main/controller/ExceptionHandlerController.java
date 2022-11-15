package main.controller;

import main.api.response.PostResponse;
import main.errors.NoPostEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoPostEntityException.class)
    public ResponseEntity<PostResponse> handleNoPostEntityException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PostResponse(
                NoPostEntityException.class.getName(),
                System.currentTimeMillis(),
                0,
                0,
                new ArrayList<>(),
                e.getMessage()));
    }
}
