package usco.edu.co.Parcial2ConsultasMedicas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/registro", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/administrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/medico/**").hasRole("MEDICO")
                        .requestMatchers("/paciente/**").hasRole("PACIENTE")
                        .requestMatchers("/api/consultas/mis-medico").hasRole("MEDICO")
                        .requestMatchers("/api/consultas/mis-paciente", "/api/consultas/*/horario").hasRole("PACIENTE")
                        .requestMatchers("/api/medicos/**", "/api/pacientes/buscar").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/consultas/**").hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/sin-permiso"))
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String redirect = authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .findFirst()
                    .map(authority -> switch (authority) {
                        case "ROLE_ADMINISTRADOR" -> "/administrador";
                        case "ROLE_MEDICO" -> "/medico";
                        case "ROLE_PACIENTE" -> "/paciente";
                        default -> "/login";
                    })
                    .orElse("/login");

            response.sendRedirect(redirect);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
