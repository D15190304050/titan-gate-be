package stark.coderaider.titan.gate.core.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class LoginStateInfo
{
    private String accessToken;
    private String refreshToken;
    private int expirationInSeconds;
    private String username;
    private String nickname;
    private List<String> roles;
}
