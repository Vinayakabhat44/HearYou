package com.antigravity.feed.aop;

import com.antigravity.feed.annotation.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    /**
     * Advice to log a successful completion of an audited method.
     */
    @AfterReturning(pointcut = "@annotation(auditLog)", returning = "result")
    public void auditAction(JoinPoint joinPoint, AuditLog auditLog, Object result) {
        String action = auditLog.value();
        String user = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "SYSTEM";

        String methodName = joinPoint.getSignature().getName();

        log.info("AUDIT: User '{}' performed action '{}' using method '{}'", user, action, methodName);
    }
}
