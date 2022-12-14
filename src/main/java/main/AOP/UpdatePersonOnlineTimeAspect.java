package main.AOP;

import lombok.RequiredArgsConstructor;
import main.repository.PersonsRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Aspect
@Component
@RequiredArgsConstructor
public class UpdatePersonOnlineTimeAspect {

    private final PersonsRepository personsRepository;

    @Value("${socialNetwork.timezone}")
    private String timezone;

    @Before("@annotation(main.AOP.annotations.UpdateOnlineTime)")
    public void updateOnlineTime() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            personsRepository.updateOnlineTime(email, LocalDateTime.now(ZoneId.of(timezone)));
        }
    }
}
