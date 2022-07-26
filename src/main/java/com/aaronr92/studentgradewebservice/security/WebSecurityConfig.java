package com.aaronr92.studentgradewebservice.security;

import com.aaronr92.studentgradewebservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.aaronr92.studentgradewebservice.util.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers(POST, "/api/user/signup").anonymous()
                .antMatchers(PUT, "/api/user/change/password").hasAnyAuthority(ROLE_TEACHER.name(), ROLE_STUDENT.name())
                .antMatchers(PUT, "/api/user/change/role").hasAnyAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(PUT, "/api/user/change/username").hasAnyAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(GET, "/api/user/find/all").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(GET, "/api/user/find").authenticated()
                .antMatchers(DELETE, "/api/user").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(POST, "/api/student/mark").hasAuthority(ROLE_TEACHER.name())
                .antMatchers(DELETE, "/api/student/mark").hasAuthority(ROLE_TEACHER.name())
                .antMatchers("/api/subject/**").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(GET, "/api/group/**").hasAnyAuthority(ROLE_TEACHER.name(), ROLE_ADMINISTRATOR.name())
                .antMatchers(POST, "/api/group").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(DELETE, "/api/group").hasAuthority(ROLE_ADMINISTRATOR.name())
                .anyRequest().denyAll();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
