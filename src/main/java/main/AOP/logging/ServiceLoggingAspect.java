package main.AOP.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {

    private LogContext logContext;

    @Pointcut(value = "within(main.service..*)")
    private void allServices() {
    }

    @Before("allServices()")
    private void beforeMethod(JoinPoint joinPoint) {
        logContext = new LogContext(joinPoint);
        log.debug("START {}.{}(" + logContext.getArgs(joinPoint) + ")", logContext.getClassName(), logContext.getMethodName());
    }

    @AfterReturning(value = "allServices()", returning = "response")
    private void afterMethod(JoinPoint joinPoint, Object response) {
        logContext = new LogContext(joinPoint);
        log.debug("FINISHED {}.{}(" + logContext.getArgs(joinPoint) + "); RESPONSE: {}",
                logContext.getClassName(),
                logContext.getMethodName(),
                response);
    }

    @AfterThrowing(value = "allServices()", throwing = "ex")
    private void afterError(JoinPoint joinPoint, Throwable ex) {
        logContext = new LogContext(joinPoint);
        log.warn("ERROR {}.{}(" + logContext.getArgs(joinPoint) + ") - {}:{}",
                logContext.getClassName(),
                logContext.getMethodName(),
                ex.getClass().getSimpleName(),
                ex.getLocalizedMessage());
    }
}
