package stark.coderaider.titan.gate.core.config.security;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.domain.dtos.UserDetailsImpl;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.coderaider.titan.gate.core.domain.dtos.responses.LoginResponse;
import stark.dataworks.boot.web.ServiceResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessJsonHandler implements AuthenticationSuccessHandler
{
    @Autowired
    private JwtService jwtService;

    @Autowired
    private TitanGateRedisOperation titanGateRedisOperation;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
//        cacheAuthentication(user);
        writeAuthenticationInfo(request, response, user);
    }

    // For SSO, we only need to return a token.
    // Then other system can get user info like username by token.
    // Without SSO, we can return all the information.
    public void writeAuthenticationInfo(HttpServletRequest request, HttpServletResponse response, UserDetailsImpl user) throws IOException
    {
//        Object redirectUrlAttribute = request.getAttribute(SecurityConstants.REDIRECT_URL);
//        String redirectUrl = redirectUrlAttribute == null ? null : (String) redirectUrlAttribute;
//        log.info("redirectUrl = {}", redirectUrl);

        LoginResponse loginResponse = generateLoginStateTokenInfo(user);

        // Set cookie for SSO.
        response.addCookie(new Cookie(SecurityConstants.SSO_COOKIE_NAME, loginResponse.getAccessToken()));

        ServiceResponse<LoginResponse> loginSuccessResponse = ServiceResponse.buildSuccessResponse(loginResponse, SecurityConstants.LOGIN_SUCCESS);
        loginSuccessResponse.writeToResponse(response);
    }

    // TODO: Cache roles in other services.
    private void cacheAuthentication(UserDetailsImpl user)
    {
        // Cache user info.
    }

    private LoginResponse generateLoginStateTokenInfo(UserDetailsImpl user)
    {
        String token = jwtService.createToken(user);
        LoginResponse loginStateToken = new LoginResponse();
        loginStateToken.setAccessToken(token);

        // TODO: Add refresh token & expiration time.

        return loginStateToken;
    }
}
