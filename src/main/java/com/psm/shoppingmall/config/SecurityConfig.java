package com.psm.shoppingmall.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.psm.shoppingmall.security.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

// spring security에 대한 설정
@Configuration
@RequiredArgsConstructor // private final 빈 등록
public class SecurityConfig {

    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // JWT 토큰 인증을 사용. 인즈에서 HttpSession을 사용하지 않는다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable() // 직접 id, pw를 입력받아서 JWT 토큰을 리턴하는 api를 직접 만든다.
            .csrf().disable() // CSRF 공격을 막기 위한 방법
            .cors()
            .and()
            .apply(authenticationManagerConfig) //
            .and()
            .httpBasic().disable() // basic 인증은 off
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // 사전 인증을 모두 허용한다.
            .mvcMatchers("/signup", "/login", "/users/refresh")
            .permitAll() // 각 api의 모든 권한을 호출 가능하도록 설정
            .mvcMatchers(GET, "/**").hasAnyRole("USER", "MANAGER", "ADMIN")
            .mvcMatchers(POST, "/**").hasAnyRole("USER", "MANAGER", "ADMIN")
            .anyRequest().hasAnyRole()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .and()
            .build();

    }

    // 암호를 암호화, 사용자가 입력한 암호가 일치하는지 검사할 때 사용하는 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
