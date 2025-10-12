package stark.coderaider.titan.gate.core.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.loginstate.UserPrincipal;
import stark.coderaider.titan.gate.core.redis.RedisKeyManager;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.coderaider.titan.gate.core.services.UserContextService;
import stark.coderaider.titan.gate.core.domain.dtos.UserInfo;
import stark.dataworks.basic.data.json.JsonSerializer;
import stark.dataworks.basic.data.redis.RedisQuickOperation;
import stark.dataworks.boot.web.TokenHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
public class TokenLoginFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisQuickOperation redisQuickOperation;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TitanGateRedisOperation titanGateRedisOperation;

    @Autowired
    private UserContextService userContextService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // Check if authentication is already set to avoid circular calls
        if (SecurityContextHolder.getContext().getAuthentication() != null)
        {
            filterChain.doFilter(request, response);
            return;
        }

        // Parse the login state if there is a token, no matter if it is a login state required Uri.
        String token = TokenHandler.getToken(request, SecurityConstants.SSO_COOKIE_NAME);

        if (StringUtils.hasText(token))
        {
            UserInfo userInfo = jwtService.parseUserInfo(token);
            if (userInfo != null)
            {
                long userId = userInfo.getUserId();
                String userJson = redisQuickOperation.get(RedisKeyManager.getUserIdKey(userId));
                UserPrincipal user;

                if (StringUtils.hasText(userJson))
                    user = JsonSerializer.deserialize(userJson, UserPrincipal.class);
                else
                {
                    try
                    {
                        user = (UserPrincipal) userDetailsService.loadUserByUsername(userInfo.getUsername());
                        titanGateRedisOperation.cacheUser(user);
                    }
                    catch (Exception e)
                    {
                        log.error("Failed to load user details for username: {}", userInfo.getUsername(), e);
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                userContextService.setCurrentUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}