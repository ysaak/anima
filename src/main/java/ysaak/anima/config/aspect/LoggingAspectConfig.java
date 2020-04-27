package ysaak.anima.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.context.annotation.Configuration;

import java.util.StringJoiner;

@Aspect
@Configuration
public class LoggingAspectConfig {
    @Around("ysaak.anima.config.aspect.PointConfig.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodInvocationProceedingJoinPoint methodJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        final MethodSignature signature = (MethodSignature) methodJoinPoint.getSignature();

        String logBase = signature.getDeclaringType().getSimpleName()
                + "." + signature.getName()
                + "(" + getMethodArgs(methodJoinPoint.getArgs()) + ")";

        Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
        logger.debug(logBase);

        long startTime = System.currentTimeMillis();
        boolean failed = false;

        final Object returnValue;
        try {
            returnValue = joinPoint.proceed();

            if (signature.getReturnType() != Void.class) {
                logger.debug("{} returning {}", logBase, returnValue);
            }
        }
        catch (Throwable t) {
            failed = true;
            throw t;
        }
        finally {
            long timeTaken = System.currentTimeMillis() - startTime;

            if (failed) {
                logger.error("{} failed in {} ms", logBase, timeTaken);
            }
            else {
                logger.debug("{} successfully in {} ms", logBase, timeTaken);
            }
        }

        return returnValue;
    }

    private String getMethodArgs(Object[] args) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        if (args != null) {
            for (Object arg : args) {
                joiner.add(String.valueOf(arg));
            }
        }

        return joiner.toString();
    }
}
