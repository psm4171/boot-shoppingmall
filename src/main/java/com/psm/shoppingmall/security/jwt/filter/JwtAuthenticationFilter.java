package com.psm.shoppingmall.security.jwt.filter;

import com.psm.shoppingmall.security.jwt.exception.JwtExceptionCode;
import com.psm.shoppingmall.security.jwt.token.JwtAuthenticationToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = "";

        try{
            token = getToken(request);
            if(StringUtils.hasText(token)){
                getAuthentication(token);
            }
            filterChain.doFilter(request, response);
        }catch (NullPointerException | IllegalStateException e){
            request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN);
            log.error("Not found Token", token);
        }

    }

    private String getToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer")){
            String[] arr = authorization.split(" ");
            return  arr[1];
        }
        return null;
    }

    private void  getAuthentication(String token){
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext()
            .setAuthentication(authenticationToken);
    }
}
