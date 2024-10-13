package com.PSM.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService myUserDetailService; // Используем ваш сервис для загрузки пользователей

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Настройка авторизации
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home","/auth/register", "/auth/login").permitAll() // Доступ к регистрации и логину без аутентификации
                        .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                )
//                .formLogin(form -> form
//                        .loginPage("/auth/login")   // Своя страница логина
//                        .defaultSuccessUrl("/home")  // Куда перенаправлять после успешного входа
//                        .failureUrl("/auth/login?error=true") // Перенаправление при ошибке входа
//                        .permitAll()
//                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(myUserDetailService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
