package soialNetworkApp.errors;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.ErrorRs;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    //400
    @ExceptionHandler({
            NoSuchEntityException.class,
            EmptyFieldException.class,
            BadCredentialsException.class,
            PasswordException.class,
            CaptchaException.class,
            FileException.class,
            WrongEmailException.class
    })
    public ResponseEntity<ErrorRs> handleBadRequest(Exception e) {
        return ResponseEntity.status(400).body(ErrorRs.builder()
                .error(NoSuchEntityException.class.getName())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build());
    }

    //401
    @ExceptionHandler({
            PersonNotFoundException.class,
            BadAuthorizationException.class
    })
    public ResponseEntity<ErrorRs> handleUnauthorized(Exception e) {
        return ResponseEntity.status(401).body(ErrorRs.builder()
                .error(e.getLocalizedMessage())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getLocalizedMessage())
                .build());
    }

    //204
    @ExceptionHandler(IncorrectRequestTypeException.class)
    public ResponseEntity<ErrorRs> handleIncorrectRequestTypeException(Exception e) {
        return ResponseEntity.status(204).body(ErrorRs.builder()
                .error(IncorrectRequestTypeException.class.getSimpleName())
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build());
    }
}
