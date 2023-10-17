package hexlet.code;
/*

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    final UserDetailsServiceImpl userDetailsService;

    // Переопределяет схему аутентификации
    //@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // BEGIN
        http.csrf(csrf -> csrf.disable()).authorizeRequests().and()
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeRequests()
            .requestMatchers(GET, "/").permitAll()
            .requestMatchers(POST, "/users").permitAll()
           // .requestMatchers(GET, "/users/**").hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
          //  .requestMatchers(DELETE, "/users/**").hasAuthority(UserRole.ADMIN.name())
            .and().httpBasic(withDefaults());

        return http.build();
        // END
    }

    // Указываем, что для сравнения хешей паролей
    // будет использоваться кодировщик BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
*/
