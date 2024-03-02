package com.psm.shoppingmall.security.jwt.token;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private Object principal; // 인증된 사용자의 주체
    private Object credentials; // 인증에 사용되는 자격 증명


    public JwtAuthenticationToken(
        Collection<? extends GrantedAuthority> authorities,
        Object principal, Object credentials) {

        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    public JwtAuthenticationToken(String token) { // JWT 토큰을 받아서 인증 객체 생성
        super(null);
        this.token = token;
        this.setAuthenticated(false); // 인증되지 않은 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
