package stark.coderaider.titan.gate.core.domain.entities.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stark.coderaider.fluentschema.commons.EntityBase;
import stark.coderaider.fluentschema.commons.NamingConvention;
import stark.coderaider.fluentschema.commons.annotations.Column;
import stark.coderaider.fluentschema.commons.annotations.Key;
import stark.coderaider.fluentschema.commons.annotations.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(namingConvention = NamingConvention.LOWER_CASE_WITH_UNDERSCORE, comment = "Business system.")
public class BusinessSystem extends EntityBase
{
    @Key(name = "idx_name")
    @Column(type = "VARCHAR(100)", nullable = false, comment = "Name of the business system.")
    private String name;

    @Key(name = "idx_code")
    @Column(type = "VARCHAR(100)", nullable = false, unique = true, comment = "Code of the business system.")
    private String code;

    @Column(type = "VARCHAR(500)", comment = "Description of the business system.")
    private String description;
}
