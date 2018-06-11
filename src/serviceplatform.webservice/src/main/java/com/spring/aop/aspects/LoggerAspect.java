package com.spring.aop.aspects;

import java.util.Arrays;
import java.util.Date;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class LoggerAspect {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Pointcut("execution(* com.oneassist.serviceplatform.services.*.*.*(..))")
    public void businessClassMethods() {

    }

    @Pointcut("execution(* com.oneassist.serviceplatform.commons.*.*.*(..))")
    public void commonsClassMethods() {

    }

    @Pointcut("execution(* com.oneassist.serviceplatform.webservice.resources.*.*(..))")
    public void resourceClassMethods() {

    }

    @Around("businessClassMethods() || resourceClassMethods()")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        StringBuffer logMessage = new StringBuffer();
        StopWatch stopWatch = new StopWatch();
        Object retVal = null;

        try {
            logMessage.append(joinPoint.getTarget().getClass().getName());
            logMessage.append(".");
            logMessage.append(joinPoint.getSignature().getName());

            logger.error(logMessage.toString() + (" -> Start Time:" + new Date()));

            stopWatch.start();
            retVal = joinPoint.proceed();
        } finally {
            stopWatch.stop();

            logger.error(logMessage.toString() + (" -> End Time:" + new Date()));

            logMessage.append(" execution time: ");
            logMessage.append(stopWatch.getTotalTimeMillis());
            logMessage.append(" ms");

            logger.error(logMessage.toString());
        }

        return retVal;
    }

    @Around("businessClassMethods() || resourceClassMethods()")
    public Object logMethodArgumentsAndResponses(ProceedingJoinPoint joinPoint) throws Throwable {

        StringBuffer logMessage = new StringBuffer();
        Object retVal = null;

        try {
            logMessage.append(joinPoint.getTarget().getClass().getName());
            logMessage.append(".");
            logMessage.append(joinPoint.getSignature().getName());

            logger.error(logMessage.toString() + (" -> Request Params :" + Arrays.toString(joinPoint.getArgs())));

            retVal = joinPoint.proceed();
        } finally {

            logger.error(logMessage.toString() + (" -> Response :" + retVal));

        }

        return retVal;
    }
}
