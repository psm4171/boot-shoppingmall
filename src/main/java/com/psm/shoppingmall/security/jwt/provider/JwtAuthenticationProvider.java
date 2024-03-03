package com.psm.shoppingmall.security.jwt.provider;

import com.psm.shoppingmall.security.jwt.filter.JwtAuthenticationFilter;
import com.psm.shoppingmall.security.jwt.token.JwtAuthenticationToken;
import com.psm.shoppingmall.security.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenizer jwtTokenizer;
    // JWT 토큰 안에 있는 사용자가 JSON 형태의 내용을 추가할 수 있다,

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        // 토큰을 검증. 기간이 만료되었는지, 토큰 문자열이 문제가 있는지 확인 후 이상 생기면 에외 발생.

        Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
        String email = claims.getSubject(); // sub에 암호화된 데이터를 넣고, 복호화하는 코드를 넣어줄 수 있다.
        List<GrantedAuthority> authrities = getGrantedAuthorities(claims);

        return new JwtAuthenticationToken(authrities, email, null);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(() -> role);
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    // JwtAuthenticationProvider는 JWT 토큰을 사용하여 사용자의 인증을 처리하고,
    // 권한 정보를 추출하여 인증 객체를 생성하는 역할


}
