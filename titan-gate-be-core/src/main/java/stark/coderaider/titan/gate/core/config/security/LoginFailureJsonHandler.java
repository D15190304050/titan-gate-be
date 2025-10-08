package stark.coderaider.titan.gate.core.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.domain.dtos.LoginStateTokenInfo;
import stark.dataworks.basic.exceptions.ExceptionInfoFormatter;
import stark.dataworks.boot.web.ServiceResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginFailureJsonHandler implements AuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException
    {
        String exceptionInfo = ExceptionInfoFormatter.formatMessageAndStackTrace(exception);
        log.error("Login failure: {}", exceptionInfo);

        ServiceResponse<LoginStateTokenInfo> loginResult = ServiceResponse.buildErrorResponse(-1, exceptionInfo);
        loginResult.writeToResponse(response);
    }
}
