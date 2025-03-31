package Spring.Visit.SharedModule.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring HTTP security settings...");

        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/reset-password.html").permitAll() // SSE endpoint requires auth
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Security filter chain configured successfully.");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating PasswordEncoder with BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        logger.info("Creating BCryptPasswordEncoder instance.");
        return new BCryptPasswordEncoder();
    }
}
