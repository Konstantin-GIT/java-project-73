/* package hexlet.code;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    final UserDetailsServiceImpl userDetailsService;

    // Переопределяет схему аутентификации
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.
                requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated())
            .addFilter(new JWTAuthenticationFilter(
                authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                loginRequest,
                jwtHelper
            ))
            .addFilterBefore(
                new JWTAuthorizationFilter(publicUrls, jwtHelper),
                UsernamePasswordAuthenticationFilter.class
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                    .sameOrigin()))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }






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


}
*/
