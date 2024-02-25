package com.psm.shoppingmall.security.jwt.exception;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        String exception = (String) request.getAttribute("exception");
        log.error("Exception : {}", exception);

        if (exception == null) {
            log.error("exception is null");
            setResponse(response, JwtExceptionCode.NOT_FOUND_TOKEN);
        } else if (exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
            log.error("invalid token");
            setResponse(response, JwtExceptionCode.INVALID_TOKEN);
        } else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
            log.error("expired token");
            setResponse(response, JwtExceptionCode.EXPIRED_TOKEN);
        }
    }

    private void setResponse(HttpServletResponse response, JwtExceptionCode exceptionCode)
        throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", exceptionCode.getMessage());
        errorInfo.put("code", exceptionCode.getCode());
        Gson gson = new Gson();
        String responseJson = gson.toJson(errorInfo);
        response.getWriter().print(responseJson);
    }
}
