package ysaak.anima.config;

import com.mitchellbosecke.pebble.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ysaak.anima.view.helper.AssetResolver;
import ysaak.anima.view.helper.ViewHelperExtension;

import javax.servlet.ServletContext;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext context;
    private final ServletContext servletContext;

    @Autowired
    public WebConfig(ApplicationContext context, ServletContext servletContext) {
        this.context = context;
        this.servletContext = servletContext;
    }

    @Bean
    public AssetResolver assetResolver () {
        return new AssetResolver("/public");
    }

    @Bean
    public Extension viewHelperExtension() {
        return new ViewHelperExtension(context);
    }

    /*
    @Override
    public void configure(JtwigViewResolver viewResolver) {

        final Reflections reflections = new Reflections(AnimaApplication.BASE_PACKAGE);
        final Set<Class<?>> viewHelperClassSet = reflections.getTypesAnnotatedWith(ViewHelper.class);

        List<JtwigFunction> functionList = viewHelperClassSet.stream()
                .filter(JtwigFunction.class::isAssignableFrom)
                .map(c -> (JtwigFunction) this.context.getBean(c))
                .collect(Collectors.toList());


        viewResolver.setRenderer(new JtwigRenderer(EnvironmentConfigurationBuilder
                .configuration()
                .functions().add(functionList).and()
                .build()));
    }
    */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("public/**").addResourceLocations("classpath:public/");
    }
}
