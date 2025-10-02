package stark.coderaider.titan.gate.core.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.domain.dtos.requests.LoginRequest;
import stark.dataworks.basic.data.json.JsonSerializer;
import stark.dataworks.boot.ExceptionLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter
{
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        // 1. Determine if it is a POST method.
        // 2. Determine if its content type is "application/json".

        // 1.
        String requestMethod = request.getMethod();
        if (!requestMethod.equalsIgnoreCase(HttpMethod.POST.name()))
            throw new AuthenticationServiceException("Authentication method not supported: " + requestMethod);

        // 2.
        String contentType = request.getContentType();
        if (contentType != null && contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
        {
            try
            {
                LoginRequest loginRequest = JsonSerializer.deserialize(request.getInputStream(), LoginRequest.class);
                String username = loginRequest.getUsername();
                String password = loginRequest.getPassword();
                String rememberMe = loginRequest.getRememberMe();
                request.setAttribute(SecurityConstants.REMEMBER_ME, rememberMe);
                UsernamePasswordAuthenticationToken authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, authenticationRequest);

                String redirectUrl = loginRequest.getRedirectUrl();
                if (StringUtils.hasText(redirectUrl))
                    request.setAttribute(SecurityConstants.REDIRECT_URL, redirectUrl);

                return this.getAuthenticationManager().authenticate(authenticationRequest);
            }
            catch (Exception e)
            {
                ExceptionLogger.logExceptionInfo(e);
                throw new AuthenticationServiceException("Failed to parse JSON authentication request", e);
            }
        }

        // Call attemptAuthentication() of super class.
        return super.attemptAuthentication(request, response);
    }
}