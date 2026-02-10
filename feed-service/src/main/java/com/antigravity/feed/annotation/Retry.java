package com.antigravity.feed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable automatic retry on failure.
 * attempts specifies the maximum number of retry attempts.
 * delay specifies the delay between attempts in milliseconds.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int attempts() default 3;

    long delay() default 1000L;
}
