package stark.coderaider.titan.gate.core.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserInfo implements UserDetails
{
    private long id;
    private String username;

    @JsonIgnore
    private String password;

    private String nickname;
    private String avatarUrl;
    private String email;
    private String gender;

    // TODO: Implement below methods.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        // For now, return a default role to prevent authorization issues
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}