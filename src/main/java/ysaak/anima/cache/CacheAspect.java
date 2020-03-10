package ysaak.anima.cache;

import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class CacheAspect {

    private final CacheService cacheService;

    @Autowired
    public CacheAspect(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(ysaak.anima.cache.Cacheable)")
    public Object cacheResult(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final Cacheable annotation = method.getAnnotation(Cacheable.class);

        final Cache cache = annotation.cache();
        final String cacheKey = getCacheKey(joinPoint.getArgs());

        Optional<Serializable> cachedData = cacheService.get(cache, cacheKey);

        if (cachedData.isPresent()) {
            return cachedData.get();
        }

        final Object result = joinPoint.proceed();

        if (result != null && Serializable.class.isAssignableFrom(result.getClass())) {
            cacheService.store(cache, cacheKey, (Serializable) result);
        }

        return result;
    }

    private String getCacheKey(Object[] args) {
        return Joiner.on('-').useForNull("").join(args);
    }
}
