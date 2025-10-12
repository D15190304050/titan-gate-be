package stark.coderaider.titan.gate.loginstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserPrincipal
{
    private long id;
    private String username;

    @JsonIgnore
    private String password;

    private String nickname;
    private String avatarUrl;
    private String email;
    private String gender;

    private List<String> authorities;

    public UserPrincipal()
    {
        authorities = new ArrayList<>();
    }
}