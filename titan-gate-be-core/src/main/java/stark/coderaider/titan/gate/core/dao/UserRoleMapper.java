package stark.coderaider.titan.gate.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stark.coderaider.titan.gate.core.domain.entities.mysql.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper
{
    UserRole getByUserAndSystem(@Param("userId") long userId, @Param("systemCode") String systemCode);

    List<UserRole> listByUser(@Param("userId") long userId);

    int insert(UserRole userRole);

    int update(UserRole userRole);
}
