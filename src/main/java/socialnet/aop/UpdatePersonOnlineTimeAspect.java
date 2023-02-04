package socialnet.aop;

import lombok.RequiredArgsConstructor;
import socialnet.repository.PersonsRepository;
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

    @Before("@annotation(socialnet.aop.annotations.UpdateOnlineTime)")
    public void updateOnlineTime() {
        String email = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            email = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        if (email != null) {
            personsRepository.updateOnlineTime(email, LocalDateTime.now(ZoneId.of(timezone)));
        }
    }
}
