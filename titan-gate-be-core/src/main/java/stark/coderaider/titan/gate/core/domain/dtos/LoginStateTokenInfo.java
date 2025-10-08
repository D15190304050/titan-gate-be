package stark.coderaider.titan.gate.core.domain.dtos;

import lombok.Data;

@Data
public class LoginStateTokenInfo
{
    private String accessToken;
    private String refreshToken;
    private int expirationInSeconds;
}
