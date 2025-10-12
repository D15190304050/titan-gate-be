package stark.coderaider.titan.gate.core.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import stark.coderaider.titan.gate.loginstate.UserPrincipal;

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

    // TODO: Implement below methods.

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

    public UserPrincipal toUserPrincipal()
    {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(id);
        userPrincipal.setUsername(username);
        userPrincipal.setPassword(password);
        userPrincipal.setNickname(nickname);
        userPrincipal.setAvatarUrl(avatarUrl);
        userPrincipal.setEmail(email);
        userPrincipal.setGender(gender);
        userPrincipal.setAuthorities(authorities.stream().map(GrantedAuthority::getAuthority).toList());
        return userPrincipal;
    }

    public static UserDetailsImpl fromUserPrincipal(UserPrincipal userPrincipal)
    {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(userPrincipal.getId());
        userDetails.setUsername(userPrincipal.getUsername());
        userDetails.setPassword(userPrincipal.getPassword());
        userDetails.setNickname(userPrincipal.getNickname());
        userDetails.setAvatarUrl(userPrincipal.getAvatarUrl());
        userDetails.setEmail(userPrincipal.getEmail());
        userDetails.setGender(userPrincipal.getGender());

        List<GrantedAuthority> authorities = getAuthorities(userPrincipal);
        userDetails.setAuthorities(authorities);

        return userDetails;
    }

    private static List<GrantedAuthority> getAuthorities(UserPrincipal userPrincipal)
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : userPrincipal.getAuthorities())
            authorities.add(new SimpleGrantedAuthority(authority));
        return authorities;
    }
}