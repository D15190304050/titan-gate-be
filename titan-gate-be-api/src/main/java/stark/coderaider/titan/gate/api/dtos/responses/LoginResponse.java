package stark.coderaider.titan.gate.api.dtos.responses;

import lombok.Data;

@Data
public class LoginResponse
{
    private String token;
    private Long userId;
    private String username;
    private String email;
}