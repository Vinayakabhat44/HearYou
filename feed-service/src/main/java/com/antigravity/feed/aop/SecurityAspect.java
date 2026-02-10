package com.antigravity.feed.aop;

import com.antigravity.feed.annotation.AuthorizeOwner;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    /**
     * Advice to check if the authenticated user matches the owner ID in the method
     * arguments.
     * Searches for a Long argument that typically represents userId.
     */
    @Before("@annotation(com.antigravity.feed.annotation.AuthorizeOwner)")
    public void authorizeResourceOwner(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("No authentication found");
        }

        String currentUserId = authentication.getName(); // In our filter, we set userId as name

        // Find the userId argument in the method
        Object[] args = joinPoint.getArgs();
        boolean authorized = Arrays.stream(args)
                .filter(arg -> arg instanceof Long)
                .map(Object::toString)
                .anyMatch(arg -> arg.equals(currentUserId));

        if (!authorized) {
            log.warn("Access denied for user {} on restricted method {}", currentUserId,
                    joinPoint.getSignature().getName());
            throw new SecurityException("Unauthorized access: User is not the resource owner");
        }

        log.debug("Authorized access for user {} on restricted method {}", currentUserId,
                joinPoint.getSignature().getName());
    }
}
