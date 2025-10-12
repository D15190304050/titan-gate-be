package stark.coderaider.titan.gate.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.dao.UserMapper;
import stark.coderaider.titan.gate.loginstate.UserPrincipal;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;

@Slf4j
@Component
public class DaoUserDetailService implements UserDetailsService, UserDetailsPasswordService
{
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword)
    {
        int updateCount = userMapper.updatePasswordByUsername(user.getUsername(), newPassword);
        if (updateCount == 1)
            ((UserPrincipal) user).setPassword(newPassword);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        try
        {
            // TODO: Call RPC interface to get nickname, avatar url, gender.

            User user = userMapper.getUserByUsername(username);
            if (user == null)
                throw new UsernameNotFoundException("User not found with username: " + username);

            UserPrincipal userPrincipal = new UserPrincipal();
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getEncryptedPassword());
            userPrincipal.setId(user.getId());
            userPrincipal.setEmail(user.getEmail());

            // TODO: Call RPC interface to get nickname, avatar url, gender.
//        userInfo.setNickname(user.getNickname());
//        userInfo.setAvatarUrl(user.getAvatarUrl());
//        userInfo.setGender(user.getGender());

            return userPrincipal;
        }
        catch (Exception e)
        {
            log.error("Error loading user by username: {}", username, e);
            throw new UsernameNotFoundException("Error loading user by username: " + username, e);
        }
    }
}