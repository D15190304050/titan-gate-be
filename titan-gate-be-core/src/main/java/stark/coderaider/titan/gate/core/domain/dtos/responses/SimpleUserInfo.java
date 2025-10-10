package stark.coderaider.titan.gate.core.domain.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class SimpleUserInfo
{
    private long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private List<String> roles;
}
