package org.example.expert.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CustomSecurityUser {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Set<GrantedAuthority> authorities;

    public CustomSecurityUser(Long userId, String email, String nickname, Set<GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.authorities = new HashSet<>(authorities);
    }
}
