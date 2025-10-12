package stark.coderaider.titan.gate.core.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import stark.coderaider.titan.gate.loginstate.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails
{
    private long id;
    private String username;

    @JsonIgnore
    private String password;

    private String nickname;
    private String avatarUrl;
    private String email;
    private String gender;

    private List<GrantedAuthority> authorities;

    public UserDetailsImpl()
    {
        authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    public UserInfo toUserInfo()
    {
        return new UserInfo(id, username, nickname);
    }
}