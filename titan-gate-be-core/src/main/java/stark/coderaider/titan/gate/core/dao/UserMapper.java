package stark.coderaider.titan.gate.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;

import java.util.List;

@Mapper
public interface UserMapper
{
    User getUserByUsername(String username);
    int updatePasswordByUsername(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);
    int insert(User user);
    List<User> getUsersByUsernamePhoneNumberEmail(@Param("username") String username, @Param("phoneNumber") String phoneNumber, @Param("phoneNumberCountryCode") String phoneNumberCountryCode, @Param("email") String email);
    User getUserById(long id);
}
