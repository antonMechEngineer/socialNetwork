package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.errors.*;
import main.model.entities.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    @ExceptionHandler(NoPostEntityException.class)
    public ResponseEntity<CommonResponse<Post>> handleNoPostEntityException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.<Post>builder()
                .error(NoPostEntityException.class.getName())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build());
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<CommonResponse<PersonResponse>> handlePersonNotFoundByEmailException(Exception e) {
        return ResponseEntity.status(401).body(CommonResponse.<PersonResponse>builder()
                .error(e.getLocalizedMessage())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(BadAuthorizationException.class)
    public ResponseEntity<CommonResponse<PersonResponse>> handleBadAuthorizationException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CommonResponse.<PersonResponse>builder()
                .error(BadAuthorizationException.class.getName())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build());
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<CommonResponse> handleEmptyFieldException(Exception e) {
        return ResponseEntity.status(400).body(CommonResponse.builder()
                .error(EmptyFieldException.class.getSimpleName())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getLocalizedMessage())
                .build());
    }
}
