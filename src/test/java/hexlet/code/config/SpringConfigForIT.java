package hexlet.code.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;

@Configuration
@Profile(TEST_PROFILE)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "hexlet.code")
@PropertySource(value = "classpath:/config/application-test.yml")
public class SpringConfigForIT {

    public static final String TEST_PROFILE = "test";
/*
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }*/
}

