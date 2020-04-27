package ysaak.anima.config.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointConfig {
    @Pointcut("execution(public * ysaak.anima..service.*.*(..))")
    public void businessService() {}
}
