package stark.coderaider.titan.gate.loginstate;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import stark.dataworks.boot.web.TokenHandler;

@Component
public class TitanLoginStateFilter extends OncePerRequestFilter
{
    @Value("${titan.gate.sso-cookie-name}")
    private String ssoCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = TokenHandler.getToken(request, ssoCookieName);
    }
}