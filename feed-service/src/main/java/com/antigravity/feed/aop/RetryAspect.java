package com.antigravity.feed.aop;

import com.antigravity.feed.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RetryAspect {

    /**
     * Advice to retry a method execution if it fails with an exception.
     */
    @Around("@annotation(retry)")
    public Object retryMethod(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        int attempts = retry.attempts();
        long delay = retry.delay();

        Throwable lastException = null;
        for (int i = 1; i <= attempts; i++) {
            try {
                return joinPoint.proceed();
            } catch (Throwable ex) {
                lastException = ex;
                log.warn("Attempt {} of {} failed for {}. Error: {}. Retrying in {}ms...",
                        i, attempts, joinPoint.getSignature().getName(), ex.getMessage(), delay);

                if (i < attempts) {
                    Thread.sleep(delay);
                }
            }
        }

        log.error("All {} attempts failed for {}.", attempts, joinPoint.getSignature().getName());
        throw lastException;
    }
}
