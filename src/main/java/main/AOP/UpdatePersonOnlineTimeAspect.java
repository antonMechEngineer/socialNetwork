package main.AOP;

import lombok.RequiredArgsConstructor;
import main.repository.PersonsRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class UpdatePersonOnlineTimeAspect {

    private final PersonsRepository personsRepository;

    @Before("@annotation(main.AOP.annotations.UpdateOnlineTime)")
    public void updateOnlineTime() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
//            personsRepository.updateOnlineTime(email, LocalDateTime.now());
        }
    }
}
