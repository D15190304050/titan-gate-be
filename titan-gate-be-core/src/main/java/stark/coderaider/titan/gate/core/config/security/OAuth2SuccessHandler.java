package stark.coderaider.titan.gate.core.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.dao.UserMapper;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;
import stark.coderaider.titan.gate.core.services.JwtService;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler
{
    @Value("${titan.gate.oauth2.callback-url}")
    private String oauth2CallbackUrl;

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
            String redirectUrl = UriComponentsBuilder.fromHttpUrl(oauth2CallbackUrl)
                    .queryParam("email", email)
                    .queryParam("username", username)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
        else
        {
            // Redirect to log in success page.

            String token = jwtService.generateToken(foundUser);
            response.addCookie(new Cookie(SecurityConstants.SSO_COOKIE_NAME, token));

            String redirectUrl = UriComponentsBuilder.fromHttpUrl(oauth2CallbackUrl)
                .queryParam("token", token)
                .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }
}