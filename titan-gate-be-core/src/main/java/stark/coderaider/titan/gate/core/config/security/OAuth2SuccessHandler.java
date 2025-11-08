package stark.coderaider.titan.gate.core.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import stark.coderaider.titan.gate.core.dao.UserMapper;
import stark.coderaider.titan.gate.core.domain.dtos.UserDetailsImpl;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;
import stark.coderaider.titan.gate.core.services.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler
{
    @Value("titan.gate.oauth2.login-success-url")
    private String redirectUrlLoginSuccess;

    @Value("titan.gate.oauth2.new-user-url")
    private String redirectUrlNewUser;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication)
        throws IOException
    {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        User foundUser = userMapper.getUserByEmail(email);

        if (foundUser == null)
        {
            // Redirect to new user page.
            String username = oAuth2User.getAttribute("login");
            String redirectUrl = UriComponentsBuilder.fromHttpUrl(redirectUrlNewUser)
                    .queryParam("email", email)
                    .queryParam("username", username)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
        else
        {
            // Redirect to log in success page.
            String token = jwtService.generateToken(foundUser);
            String redirectUrl = UriComponentsBuilder.fromHttpUrl(redirectUrlLoginSuccess)
                    .queryParam("token", token)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }
}