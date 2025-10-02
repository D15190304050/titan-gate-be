package stark.coderaider.titan.gate.core.services;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stark.coderaider.titan.gate.api.dtos.responses.AuthResult;
import stark.coderaider.titan.gate.api.dtos.requests.LoginRequest;
import stark.coderaider.titan.gate.api.dtos.responses.LoginResponse;
import stark.coderaider.titan.gate.api.dtos.requests.OAuth2LoginRequest;
import stark.coderaider.titan.gate.api.IAuthenticationService;
import stark.coderaider.titan.gate.core.utils.JwtUtil;

@DubboService
@Service
public class AuthenticationService implements IAuthenticationService
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private OAuth2Service oauth2Service;

    @Override
    public AuthResult<LoginResponse> login(LoginRequest request)
    {
        try
        {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 设置认证信息到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成JWT token
            String token = jwtUtil.generateToken(request.getUsername());

            // 构造响应
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(request.getUsername());
            // 在实际应用中，这里应该从数据库获取用户ID和邮箱等信息
            response.setUserId(1L);
            response.setEmail("user@example.com");

            return AuthResult.success(response);
        }
        catch (Exception e)
        {
            return AuthResult.error("登录失败: " + e.getMessage());
        }
    }

    @Override
    public AuthResult<LoginResponse> oauth2Login(OAuth2LoginRequest request)
    {
        try
        {
            if ("github".equals(request.getProvider()))
            {
//                return oauth2Service.handleGitHubLogin(request.getCode(), request.getRedirectUri());
                return null;
            }
            else
            {
                return AuthResult.error("不支持的OAuth2提供方: " + request.getProvider());
            }
        }
        catch (Exception e)
        {
            return AuthResult.error("OAuth2登录失败: " + e.getMessage());
        }
    }

    @Override
    public AuthResult<Boolean> validateToken(String token)
    {
        try
        {
            boolean isValid = jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token);
            return AuthResult.success(isValid);
        }
        catch (Exception e)
        {
            return AuthResult.success(false);
        }
    }

    @Override
    public AuthResult<Boolean> logout(String token)
    {
        // JWT是无状态的，服务器端不需要特殊处理登出
        // 在实际应用中，可能需要将token加入黑名单
        return AuthResult.success(true);
    }
}