package main.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.PersonResponse;
import main.errors.NoPostEntityException;
import main.errors.PersonNotFoundByEmailException;
import main.model.entities.Post;
import main.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    private final AuthService authService;

    @ExceptionHandler(NoPostEntityException.class)
    public ResponseEntity<CommonResponse<Post>> handleNoPostEntityException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.<Post>builder()
                .error(NoPostEntityException.class.getName())
                .timestamp(System.currentTimeMillis())
                .data(new Post())
                .errorDescription(e.getMessage())
                .build());
    }

    @ExceptionHandler(PersonNotFoundByEmailException.class)
    public ResponseEntity<CommonResponse<PersonResponse>> handlePersonNotFoundByEmailException(Exception e) {
        return ResponseEntity.ok().body(CommonResponse.<PersonResponse>builder()
                .error(e.getLocalizedMessage())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getLocalizedMessage())
                .build());
    }

}
