package com.kravchenko.photoapp.api.users.security;

import com.kravchenko.photoapp.api.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private Environment environment;
    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment environment, UsersService usersService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usersService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService, environment,
                authenticationManager);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(new AntPathRequestMatcher("/users", "POST"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/status/check", "GET")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/users/login", "POST")).permitAll()
//                        .access(new WebExpressionAuthorizationManager(
//                                "hasIpAddress('" + environment.getProperty("gateway.ip") + "')"))
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
