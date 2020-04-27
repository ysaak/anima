package ysaak.anima.config;

import com.mitchellbosecke.pebble.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ysaak.anima.config.aspect.LoggingInterceptor;
import ysaak.anima.view.helper.AssetResolver;
import ysaak.anima.view.helper.ViewHelperExtension;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext context;

    @Autowired
    public WebConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public AssetResolver assetResolver () {
        return new AssetResolver("/public");
    }

    @Bean
    public Extension viewHelperExtension() {
        return new ViewHelperExtension(context);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("public/**").addResourceLocations("classpath:public/");
        registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:favicon.ico");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
    }
}
