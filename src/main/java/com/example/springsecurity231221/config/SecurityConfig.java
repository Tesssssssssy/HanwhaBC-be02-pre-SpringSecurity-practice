package com.example.springsecurity231221.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
    @Bean
    AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity.csrf().disable()
                    // cross site 요청 변조 공격 막겠다.

                    .authorizeRequests()
                    // 인증 받는 요청만 받겠다.
                    .antMatchers("/login", "/member/signup", "/member/login").permitAll()
                    .antMatchers("/member/mypage").hasRole("USER")
                    // ()안에 아무 것도 없으므로 일단 어떤 것도 허용 x
                    // ()안에 있는 요청들만 허용하겠다.
                    // 특정 페이지에 갈 수 있는 설정
                    .anyRequest().authenticated();
                    // 어떤 요청들도 허용하곘다.
                    // 특정 페이지에 갈 수 없는 설정

            httpSecurity.formLogin().disable();

            /*
            httpSecurity.formLogin()
                    .loginPage("/test")
                    .loginProcessingUrl("/member/login")
                    .defaultSuccessUrl("/member/mypage");

            더 이상 Spring에서 제공하는 form Login 페이지를 사용하지 않겠다.
            그래서 위에서 disable(); 처리.
            그럼 사용자에게 요청을 받아서 처리하는 부분이 사라진 것.
            */

            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
