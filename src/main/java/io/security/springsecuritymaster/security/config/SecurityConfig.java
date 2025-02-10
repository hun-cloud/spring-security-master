package io.security.springsecuritymaster.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .rememberMe(rememberMe -> rememberMe
                        .alwaysRemember(true) // 기억하기(remember-me) 매개변수가 설정되지 않았을 때에도 쿠키가 항상 생성되어야 하는지에 대한 여부를 나타낸다.
                        .tokenValiditySeconds(3600) // 토큰이 유요한 시간(초 단위)를 저장할 수 있다)
                        .userDetailsService(userDetailsService()) // UserDetails를 조회하기 위해 사용되는 UserDetailsService를 지정한다.
                        .rememberMeParameter("remember") // 로그인 시 사용자를 기억하기 위해 사용되는 HTTP 매개변수이며 기본적으로 remember-me이다.
                        .rememberMeCookieName("remember") // 기억하기(remember-me) 인증을 위한 토큰을 저장하는 쿠키 이름이며 기본 값은 remember-me 이다.
                        .key("security") // 기억하기(remember-me) 인증을 위해 생성된 토큰을 식별하는 키를 설정한다.
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}
