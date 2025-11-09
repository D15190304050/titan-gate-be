package stark.coderaider.titan.gate.core.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.loginstate.UserInfo;
import stark.dataworks.boot.autoconfig.web.LogArgumentsAndResponse;
import stark.dataworks.boot.web.ServiceResponse;
import stark.dataworks.boot.web.TokenHandler;

@Slf4j
@Service
@LogArgumentsAndResponse
public class SsoService
{
    @Autowired
    private JwtService jwtService;

    public ServiceResponse<UserInfo> validateToken(HttpServletRequest request)
    {
        String token = TokenHandler.getTokenFromRequest(request, SecurityConstants.SSO_COOKIE_NAME);
        UserInfo userInfo = jwtService.parseUserInfo(token);

        boolean valid = userInfo != null;
        if (valid)
            return ServiceResponse.buildSuccessResponse(userInfo);
        return ServiceResponse.buildErrorResponse(-1, "Invalid token.");
    }
}
