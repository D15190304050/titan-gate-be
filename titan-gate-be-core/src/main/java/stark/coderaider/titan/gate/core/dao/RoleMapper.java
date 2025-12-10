package stark.coderaider.titan.gate.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stark.coderaider.titan.gate.core.domain.entities.mysql.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper
{
    Role getById(@Param("id") long id);
    Role getByCode(@Param("code") String code);
    List<Role> getByCodes(@Param("codes") Collection<String> codes);
    List<Role> listBySystem(@Param("systemCode") String systemCode);
    List<Role> listAll();
    int insert(Role role);
    int update(Role role);
    int deleteByIds(@Param("ids") Set<Long> ids);
    List<Role> getByIds(@Param("ids") Set<Long> ids);
}
