package stark.coderaider.titan.gate.core.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stark.coderaider.titan.gate.core.config.security.OAuth2SuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The {@link DebugController} provides debugging endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/debug")
public class DebugController
{
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    /**
     * The endpoint for debugging OAuth2 login success for an existing user.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/oauth2/login-success")
    public void oauth2LoginSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // For debugging purpose, change the email to an existing user's email.

        Map<String, Object> attributes = Map.of("email", "123@qq.com", "login", "testUser");
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(new HashSet<>(), attributes, "login");

        TestingAuthenticationToken authentication = new TestingAuthenticationToken(oAuth2User, null);
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * The endpoint for debugging OAuth2 login success for a new user.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/oauth2/login-not-registered")
    public void oauth2LoginNotRegistered(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // For debugging purpose, change the email to a non-existing user's email.

        Map<String, Object> attributes = Map.of("email", "1234@qq.com", "login", "testUser");
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(new HashSet<>(), attributes, "login");

        TestingAuthenticationToken authentication = new TestingAuthenticationToken(oAuth2User, null);
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
