package hexlet.code.config.rollbar;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@ComponentScan({

// UPDATE TO YOUR PROJECT PACKAGE
    "hexlet.code",
    "com.rollbar.spring"

})
public class RollbarConfig {

    // Добавляем токен через переменные окружения
    @Value("${rollbar_token:}")
    private String rollbarToken;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    /**
     * Создает и возвращает экземпляр Rollbar с использованием предоставленных конфигураций.
     *
     * @return Экземпляр Rollbar.
     */
    @Bean
    public Rollbar rollbar() {

        return new Rollbar(getRollbarConfigs(rollbarToken));
    }

    /**
     * Создает и возвращает конфигурации Rollbar с использованием предоставленного токена доступа.
     *
     * @param accessToken Токен доступа для настройки Rollbar.
     * @return Конфигурации Rollbar.
     */
    private Config getRollbarConfigs(String accessToken) {

        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("development")
                .enabled(activeProfile.equals("dev"))
                .build();
    }
}
