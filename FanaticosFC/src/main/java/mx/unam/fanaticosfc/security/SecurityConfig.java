package mx.unam.fanaticosfc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/img/**", "/js/**", "/vendor/**", "/", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página de login personalizada
                        .defaultSuccessUrl("/", true) // Redirección después del login exitoso
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL que espera el backend para logout
                        .logoutSuccessUrl("/login") // Redirección tras el logout
                        .invalidateHttpSession(true) // Invalida sesión actual
                        .deleteCookies("JSESSIONID") // Borra la cookie de sesión
                        .permitAll() // Permitir acceso a cualquiera
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Ejemplo con un usuario en memoria:
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("u")
                .password("1")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
