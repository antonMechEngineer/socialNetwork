package main.AOP.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RepositoriesLoggingAspect {

    private LogContext logContext;

    private String interfaceName;

    @Pointcut(value = "target(org.springframework.data.repository.Repository)")
    private void allRepositories() {
    }

    @Before("allRepositories()")
    private void beforeMethod(JoinPoint joinPoint) {
        logContext = new LogContext(joinPoint);
        interfaceName = Arrays.stream(joinPoint.getTarget().getClass().getInterfaces()).findFirst().get().getSimpleName();
        log.debug("START {}.{}(" + logContext.getArgs(joinPoint) + ")", interfaceName, logContext.getMethodName());
    }

    @AfterReturning(value = "allRepositories()", returning = "response")
    private void afterMethod(JoinPoint joinPoint, Object response) {
        logContext = new LogContext(joinPoint);
        interfaceName = Arrays.stream(joinPoint.getTarget().getClass().getInterfaces()).findFirst().get().getSimpleName();
        log.debug("FINISHED {}.{}(" + logContext.getArgs(joinPoint) + "); RESPONSE: {}",
                interfaceName,
                logContext.getMethodName(),
                response);
    }

    @AfterThrowing(value = "allRepositories()", throwing = "ex")
    private void afterError(JoinPoint joinPoint, Throwable ex) {
        logContext = new LogContext(joinPoint);
        log.warn("Error {}.{}(" + logContext.getArgs(joinPoint) + ") - {}:{}",
                logContext.getClassName(),
                logContext.getMethodName(),
                ex.getClass().getSimpleName(),
                ex.getLocalizedMessage());
    }
}
