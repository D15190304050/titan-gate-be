package stark.coderaider.titan.gate.loginstate;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TitanLoginStateFilter extends OncePerRequestFilter
{
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_NAME = "X-User-Name";
    public static final String HEADER_USER_NICKNAME = "X-User-Nickname";

    @Autowired
    private UserContextService userContextService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // Set login state if the request contains login state.
        String userIdString = request.getHeader(HEADER_USER_ID);
        if (userIdString != null)
        {
            long userId = Long.parseLong(userIdString);
            UserInfo userInfo = new UserInfo(userId, request.getHeader(HEADER_USER_NAME), request.getHeader(HEADER_USER_NICKNAME), new ArrayList<>());
            userContextService.setCurrentUser(userInfo);
        }

        filterChain.doFilter(request, response);
    }
}