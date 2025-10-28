package stark.coderaider.titan.gate.loginstate;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserContextCleanFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException
    {
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            // 无论业务是否正常、是否抛异常，都清理
            UserContextService.clear();
        }
    }
}