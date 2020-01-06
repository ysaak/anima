package ysaak.anima;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringContextBean implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static Optional<ApplicationContext> getContext() {
        return Optional.ofNullable(applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextBean.applicationContext = applicationContext;
    }
}
