package com.fiap.pettrack.config;

import com.fiap.pettrack.service.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                
                // Rotas de documentação Swagger / OpenAPI
                .requestMatchers("/swagger/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                
                // Rotas de autenticação e erro
                .requestMatchers("/login", "/registro", "/error", "/403").permitAll()
                
                // Endpoints REST Públicos para CRUD, Integração e Demonstração DevOps
                .requestMatchers("/pet/**", "/tutor/**", "/clinica/**", "/collar/**", "/adesao/**", "/alerta/**", "/evento/**", "/medicamento/**", "/notificacao/**", "/protocolo/**", "/score/**", "/bcs/**").permitAll()
                
                // Rotas exclusivas de Administração web
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "VET")
                
                // Rotas do portal do Tutor web
                .requestMatchers("/portal-tutor/**").hasRole("TUTOR")
                
                // Dashboard geral
                .requestMatchers("/", "/dashboard").authenticated()

                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("senha")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );

        return http.build();
    }
}
