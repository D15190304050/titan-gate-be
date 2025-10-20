package stark.coderaider.titan.gate.loginstate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo
{
    private long id;
    private String username;
    private String nickname;
    private List<String> authorities;
}
