package main.AOP.logging;

import lombok.Getter;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.StringJoiner;

@Getter
public class LogContext {

    private final String className;
    private final String methodName;

    public LogContext(JoinPoint joinPoint) {
        this.className = joinPoint.getTarget().getClass().getSimpleName();
        this.methodName = joinPoint.getSignature().getName();
    }

    public String getArgs(JoinPoint joinPoint) {
        Object[] argsList = joinPoint.getArgs();
        StringJoiner joiner = new StringJoiner(", ");
        Arrays.stream(argsList).forEach(arg -> {
            if (arg != null) {
                joiner.add(arg.getClass().getSimpleName() + " = " + arg);
            }
        });
        return joiner.toString();
    }
}
