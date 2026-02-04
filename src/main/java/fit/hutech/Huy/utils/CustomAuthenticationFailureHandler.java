package fit.hutech.Huy.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Login failed: {}", exception.getMessage());
        exception.printStackTrace();
        String errorMessage = "Login failed";
        if (exception.getMessage() != null) {
            errorMessage = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
        }
        response.sendRedirect("/login?error=" + errorMessage);
    }
}
