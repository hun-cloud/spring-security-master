package io.security.springsecuritymaster.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .formLogin(form -> form
                        .loginPage("/loginPage") // 사용자 정의 로그인 페이지로 전환, 기본 로그인 페이지 무시
                        .loginProcessingUrl("/loginProc") // 사용자 이름과 비밀번호를 검증할 URL 지정 (Form action)
                        .defaultSuccessUrl("/", true) // 로그인 성공 이후 이동 페이지, alwaysUse가
                        // true면 무조건 지정된 페이지로 이동 (기본은 false)
                        // 인증 전에 보안이 필요한 페이지를 방문하다가 인증에 성공한 경우이면 이전 위치로 리다이렉트 됨

                        .failureUrl("/failed") // 인증이 실패할 경우 사용자에게 보내질 URL을 지정, 기본값은 /login?error
                        .usernameParameter("userId") // 인증을 수행할 때 사용자 이름(아이디)를 찾기 위해 확인하는 http 매개변수 설정, 기본값은 username
                        .passwordParameter("passwd") // 인증을 수행할 때 비밀번호를 찾기 위해 확인하는 HTTP 매개변수 설정, 기본값은 password
                        .successHandler((request, response, authentication) -> {
                            System.out.println("authentication : " + authentication);
                            response.sendRedirect("/home");
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll() // failureUrl(), loginPage(), loginProcessingUrl()에 대한 URL에 모든 사용자의 접근을 허용함
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
