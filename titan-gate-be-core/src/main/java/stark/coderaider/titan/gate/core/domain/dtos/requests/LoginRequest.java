package stark.coderaider.titan.gate.core.domain.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest
{
    private String username;
    private String password;
    private String redirectUrl;
}
