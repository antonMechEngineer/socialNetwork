package main.controller;

import main.api.response.CommonResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoPostEntityException.class)
    public ResponseEntity<CommonResponse<Post>> handleNoPostEntityException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.<Post>builder()
                .error(NoPostEntityException.class.getName())
                .timestamp(System.currentTimeMillis())
                .data(new Post())
                .errorDescription(e.getMessage())
                .build());
    }
}
