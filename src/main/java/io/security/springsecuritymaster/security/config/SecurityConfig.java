package io.security.springsecuritymaster.security.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/logoutSuccess").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃이 발생하는 URL을 지정한다 (기본값은 "/logout" 이다)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        // 로그아웃이 발생하는 RequestMatcher을 지정한다. logoutUrl 보다 우선적이다.
                        // Method를 지정하지 않으면 logout URL 어떤 HTTP 메서드로든 요청될 때 로그아웃 할 수 있다.
                        .logoutSuccessUrl("/logoutSuccess") // 로그아웃이 발생한 후 리다이렉션될 URL이다. 기본값은 "/login?logout"이다.
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/logoutSuccess");
                        }) // 이것이 지정되면 logoutSuccessUrl은 무시된다.
                        .deleteCookies("JESSIONID", "remember-me") // 로그아웃 성공 시 제거될 쿠키의 이름을 지정할 수 있다.
                        .invalidateHttpSession(true) // HttpSession을 무효화해야하는 경우 true(기본값), 그렇지 않으면 false 이다.
                        .clearAuthentication(true) // 로그아웃 시 SecurityContextLogoutHandler가 인증(Authentication)을 삭제 해야 하는지 여부를 명시한다.
                        .addLogoutHandler((request, response, authentication) -> { // 기존의 로그아웃 핸들러 뒤에 새로운 LogoutHandler를 추가한다.
                            HttpSession session = request.getSession();
                            session.invalidate();
                            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
                            SecurityContextHolder.getContextHolderStrategy().clearContext();
                        })
                        .permitAll() // logoutUrl(), RequestMatcher()의 URL에 대한 모든 사용자의 접근 허용함
                )
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}
