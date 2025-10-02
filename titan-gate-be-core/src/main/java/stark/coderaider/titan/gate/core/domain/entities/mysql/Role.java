package stark.coderaider.titan.gate.core.domain.entities.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stark.coderaider.fluentschema.commons.EntityBase;
import stark.coderaider.fluentschema.commons.NamingConvention;
import stark.coderaider.fluentschema.commons.annotations.Column;
import stark.coderaider.fluentschema.commons.annotations.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(namingConvention = NamingConvention.LOWER_CASE_WITH_UNDERSCORE)
public class Role extends EntityBase
{
    @Column(type = "VARCHAR(50)", nullable = false, unique = true, comment = "角色标识")
    private String code;           // 角色标识 (ADMIN / CONSUMER / MERCHANT)

    @Column(type = "VARCHAR(50)", nullable = false, comment = "角色名称")
    private String name;           // 角色名称

    @Column(type = "VARCHAR(255)", comment = "描述")
    private String description;    // 描述
}
