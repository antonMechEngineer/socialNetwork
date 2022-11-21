package main.AOP.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    private LogContext logContext;

    @Pointcut(value = "within(main.controller..*)")
    private void allControllers() {
    }

    @Before("allControllers()")
    private void beforeMethod(JoinPoint joinPoint) {
        logContext = new LogContext(joinPoint);
        log.info("START {}.{}(" + logContext.getArgs(joinPoint) + ")", logContext.getClassName(), logContext.getMethodName());
    }

    @AfterReturning(value = "allControllers()", returning = "response")
    private void afterMethod(JoinPoint joinPoint, Object response) {
        logContext = new LogContext(joinPoint);
        log.info("FINISHED {}.{}(" + logContext.getArgs(joinPoint) + "); RESPONSE: {}",
                logContext.getClassName(),
                logContext.getMethodName(),
                response);
    }

    @AfterThrowing(value = "allControllers()", throwing = "ex")
    private void afterError(JoinPoint joinPoint, Throwable ex) {
        logContext = new LogContext(joinPoint);
        log.warn("Error {}.{}(" + logContext.getArgs(joinPoint) + ") - {}:{}",
                logContext.getClassName(),
                logContext.getMethodName(),
                ex.getClass().getSimpleName(),
                ex.getLocalizedMessage());
    }
}
