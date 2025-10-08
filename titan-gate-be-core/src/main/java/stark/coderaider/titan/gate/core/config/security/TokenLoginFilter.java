package stark.coderaider.titan.gate.core.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.domain.dtos.UserInfo;
import stark.coderaider.titan.gate.core.redis.RedisKeyManager;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.coderaider.titan.gate.core.services.UserContextService;
import stark.coderaider.titan.gate.core.domain.dtos.UserPrincipal;
import stark.dataworks.basic.data.json.JsonSerializer;
import stark.dataworks.basic.data.redis.RedisQuickOperation;
import stark.dataworks.boot.web.TokenHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
public class TokenLoginFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final RedisQuickOperation redisQuickOperation;
    private final UserDetailsService userDetailsService;
    private final TitanGateRedisOperation titanGateRedisOperation;

    public TokenLoginFilter(JwtService jwtService, RedisQuickOperation redisQuickOperation, UserDetailsService userDetailsService, TitanGateRedisOperation titanGateRedisOperation)
    {
        this.jwtService = jwtService;
        this.redisQuickOperation = redisQuickOperation;
        this.userDetailsService = userDetailsService;
        this.titanGateRedisOperation = titanGateRedisOperation;
    }

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
            UserPrincipal userPrincipal = jwtService.parseUserPrincipal(token);
            if (userPrincipal != null)
            {
                long userId = userPrincipal.getUserId();
                String userJson = redisQuickOperation.get(RedisKeyManager.getUserIdKey(userId));
                UserInfo user;

                if (StringUtils.hasText(userJson))
                    user = JsonSerializer.deserialize(userJson, UserInfo.class);
                else
                {
                    try
                    {
                        user = (UserInfo) userDetailsService.loadUserByUsername(userPrincipal.getUsername());
                        titanGateRedisOperation.cacheUser(user);
                    }
                    catch (Exception e)
                    {
                        log.error("Failed to load user details for username: {}", userPrincipal.getUsername(), e);
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                UserContextService.setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}