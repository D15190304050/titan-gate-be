package stark.coderaider.titan.gate.loginstate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo
{
    private long id;
    private String username;
    private String nickname;
}
