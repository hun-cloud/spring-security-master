package io.security.springsecuritymaster.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class AccountDto {
    private String username;
    private String pasword;
    private Collection<GrantedAuthority> authorities;

    public boolean isAccountNonExpired() {
        return true;
    }
}
