package stark.coderaider.titan.gate.api;

import stark.coderaider.titan.gate.api.dtos.responses.AuthResult;
import stark.coderaider.titan.gate.api.dtos.requests.LoginRequest;
import stark.coderaider.titan.gate.api.dtos.responses.LoginResponse;
import stark.coderaider.titan.gate.api.dtos.requests.OAuth2LoginRequest;

public interface IAuthenticationService
{
    /**
     * 用户名密码登录
     */
    AuthResult<LoginResponse> login(LoginRequest request);

    /**
     * OAuth2登录
     */
    AuthResult<LoginResponse> oauth2Login(OAuth2LoginRequest request);

    /**
     * 验证token
     */
    AuthResult<Boolean> validateToken(String token);

    /**
     * 登出
     */
    AuthResult<Boolean> logout(String token);
}