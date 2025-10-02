package stark.coderaider.titan.gate.core.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPrincipal
{
    private long accountId;
    private String username;
    private String nickname;
}
