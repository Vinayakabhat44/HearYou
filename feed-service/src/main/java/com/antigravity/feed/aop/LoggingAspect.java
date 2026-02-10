package com.antigravity.feed.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut for all methods in the service package.
     */
    @Pointcut("execution(* com.antigravity.feed.service.*.*(..))")
    public void serviceLayer() {
    }

    /**
     * Pointcut for methods annotated with @TrackTime.
     */
    @Pointcut("@annotation(com.antigravity.feed.annotation.TrackTime)")
    public void trackTimePointcut() {
    }

    /**
     * Advice to log method entry, exit and execution time for annotated methods.
     */
    @Around("serviceLayer() || trackTimePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Entering {}.{}", className, methodName);
        long start = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.info("Exiting {}.{} (took {}ms)", className, methodName, executionTime);
        }

        return result;
    }
}
