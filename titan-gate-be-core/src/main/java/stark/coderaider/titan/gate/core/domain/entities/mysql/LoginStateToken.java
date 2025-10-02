package stark.coderaider.titan.gate.core.domain.entities.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stark.coderaider.fluentschema.commons.EntityBase;
import stark.coderaider.fluentschema.commons.NamingConvention;
import stark.coderaider.fluentschema.commons.annotations.Column;
import stark.coderaider.fluentschema.commons.annotations.Table;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(namingConvention = NamingConvention.LOWER_CASE_WITH_UNDERSCORE, comment = "Tokens of login states.")
public class LoginStateToken extends EntityBase
{
    @Column(nullable = false, comment = "User ID.")
    private long userId;

    @Column(type = "VARCHAR(500)", nullable = false, comment = "The JWT Token.")
    private String token;

    @Column(nullable = false, comment = "Expiration time.")
    private Date expirationTime;

    @Column(nullable = false, comment = "Whether the token is valid.")
    private boolean valid;
}
