package com.maveric.csp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.maveric.csp.constants.Constants.LOGGING_AFTER_RETURNING;
import static com.maveric.csp.constants.Constants.LOGGING_BEFORE;


@Aspect
@Component
public class Logging {
private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Before(LOGGING_BEFORE)
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Entering " + className + "." + methodName);
    }

    @AfterReturning(pointcut = LOGGING_AFTER_RETURNING, returning = "result")
    public void afterMethodReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Exiting " + className + "." + methodName + " with result: " + result);

    }
}
