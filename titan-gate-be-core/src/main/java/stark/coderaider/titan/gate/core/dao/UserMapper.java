package stark.coderaider.titan.gate.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;

@Mapper
public interface UserMapper
{
    User getUserByUsername(String username);
    int updatePasswordByUsername(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);
}
