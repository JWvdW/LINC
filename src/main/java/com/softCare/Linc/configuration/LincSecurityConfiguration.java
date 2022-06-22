package com.softCare.Linc.configuration;

import com.softCare.Linc.service.LincUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class LincSecurityConfiguration {

    final LincUserDetailService lincUserDetailsService;

    public LincSecurityConfiguration(LincUserDetailService lincUserDetailsService) {
        this.lincUserDetailsService = lincUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                    .antMatchers("/css/**", "/webjars/**").permitAll()
                    .antMatchers("/", "user/new").permitAll()
                    .anyRequest().authenticated()
            )
                .formLogin().and()
                .logout().logoutSuccessUrl("/");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(lincUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}