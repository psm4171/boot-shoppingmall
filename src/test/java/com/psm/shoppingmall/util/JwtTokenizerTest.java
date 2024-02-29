package com.psm.shoppingmall.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTokenizerTest {

    @Autowired
    JwtTokenizer jwtTokenizer;

    @Value("${jwt.secretKey}")
    String accessSecret;
    public final Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30분

    @Test
    public void createToken() throws Exception {
        String email = "kkjjhh4171@naver.com";
        List<String> roles = List.of("ROLE_USER"); // [ROLE_USER]
        Long id = 1L;

        Claims claims = Jwts.claims().setSubject(email);// JWT토큰의 페이로드에 들어갈 내용을 설정하는 부분

        claims.put("roles", roles);
        claims.put("id", id);

        // application.yml 파일의 jwt: secretKey: 값
        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);

        // JWT를 생성하는 부분
        String JwtToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() * this.ACCESS_TOKEN_EXPIRE_COUNT)) // 현재부터 30분 뒤 만료
            .signWith(Keys.hmacShaKeyFor(accessSecret)) // 결과에 서명까지 포함시킨 JwtBuilder를 리턴
            .compact(); // 출력해놓은 토큰을 리턴

        System.out.println(JwtToken);

    }

    @Test
    public void parseToken() throws Exception {
        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        String jwtToken = "";

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(accessSecret))
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();

        System.out.println(claims.getSubject());
        System.out.println(claims.get("roles"));
        System.out.println(claims.get("userId"));
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());

    }

}
