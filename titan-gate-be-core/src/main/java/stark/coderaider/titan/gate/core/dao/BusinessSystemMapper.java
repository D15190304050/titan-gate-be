package stark.coderaider.titan.gate.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stark.coderaider.titan.gate.api.dtos.requests.ListBusinessSystemsRequest;
import stark.coderaider.titan.gate.core.domain.entities.mysql.BusinessSystem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// TODO: Remove unused methods.
@Mapper
public interface BusinessSystemMapper
{
    BusinessSystem getById(@Param("id") long id);
    boolean existsByCodeOrName(@Param("code") String  code, @Param("name") String name);
    BusinessSystem getByCode(@Param("code") String code);
    List<BusinessSystem> getByCodes(@Param("codes") Collection<String> codes);
    List<BusinessSystem> getBusinessSystems(ListBusinessSystemsRequest request);
    int insert(BusinessSystem businessSystem);
    int updateById(BusinessSystem businessSystem);
    int deleteByIds(@Param("ids") Set<Long> ids);
    List<BusinessSystem> getByIds(@Param("ids") Set<Long> ids);
}