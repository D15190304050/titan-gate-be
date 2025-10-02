package stark.coderaider.titan.gate.api.dtos.requests;

import lombok.Data;

@Data
public class OAuth2LoginRequest
{
    private String provider;  // OAuth2提供方 (github, google, wechat, apple...)
    private String code;      // 授权码
    private String redirectUri; // 重定向URI
}