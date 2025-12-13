package stark.coderaider.titan.gate.core.domain.dtos.responses;

import lombok.Data;

@Data
public class LoginResponse
{
    private String accessToken;
    private String refreshToken;
//    private int expirationInSeconds;
}
