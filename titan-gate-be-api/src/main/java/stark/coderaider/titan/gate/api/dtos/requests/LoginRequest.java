package stark.coderaider.titan.gate.api.dtos.requests;

import lombok.Data;

@Data
public class LoginRequest
{
    private String username;
    private String password;
}