package stark.coderaider.titan.gate.core.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.domain.dtos.UserInfo;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.coderaider.titan.gate.core.domain.dtos.LoginStateInfo;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;
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
    private TitanGateRedisOperation redisOperation;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        cacheAuthentication(user);
        writeAuthenticationInfo(request, response, user);
    }

    public String prepareUserLoginInfoToken(User user)
    {
        return "";
    }

    // For SSO, we only need to return a token.
    // Then other system can get user info like username by token.
    // Without SSO, we can return all the information.
    public void writeAuthenticationInfo(HttpServletRequest request, HttpServletResponse response, UserInfo user) throws IOException
    {
        Object redirectUrlAttribute = request.getAttribute(SecurityConstants.REDIRECT_URL);
        String redirectUrl = redirectUrlAttribute == null ? null : (String) redirectUrlAttribute;
        log.info("redirectUrl = {}", redirectUrl);

        LoginStateInfo loginStateInfo = generateLoginStateInfo(user);
        ServiceResponse<LoginStateInfo> loginSuccessResponse = ServiceResponse.buildSuccessResponse(loginStateInfo, SecurityConstants.LOGIN_SUCCESS);
        loginSuccessResponse.writeToResponse(response);
    }

    // TODO: Move this method to another class after integration of other login methods.
    private void cacheAuthentication(UserInfo user)
    {
        // Cache user info.
//        redisOperation.cacheUser(user);
    }

    private LoginStateInfo generateLoginStateInfo(UserInfo user)
    {
        String token = jwtService.createToken(user);
        LoginStateInfo loginStateToken = new LoginStateInfo();
        loginStateToken.setAccessToken(token);
        return loginStateToken;
    }
}
