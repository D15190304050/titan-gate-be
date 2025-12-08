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
public class UserRole extends EntityBase
{
    @Column(nullable = false, comment = "User ID.")
    private long userId;

    @Column(type = "VARCHAR(100)", nullable = false, comment = "System code.")
    private String systemCode;

    @Column(type = "VARCHAR(500)", nullable = false, comment = "Role IDs.")
    private String roleIds;
}
