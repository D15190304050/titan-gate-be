package stark.coderaider.titan.gate.core.domain.entities.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stark.coderaider.fluentschema.commons.EntityBase;
import stark.coderaider.fluentschema.commons.NamingConvention;
import stark.coderaider.fluentschema.commons.annotations.Column;
import stark.coderaider.fluentschema.commons.annotations.Table;

// TODO: Add nickname.
@Data
@EqualsAndHashCode(callSuper = true)
@Table(namingConvention = NamingConvention.LOWER_CASE_WITH_UNDERSCORE, comment = "Information that used for user login.")
public class User extends EntityBase
{
    @Column(type = "VARCHAR(64)", nullable = false, unique = true, comment = "Login name.")
    private String username;       // 登录名（唯一）

    @Column(type = "VARCHAR(128)", nullable = false, comment = "Encrypted password.")
    private String encryptedPassword;   // 密码哈希

    @Column(nullable = false, comment = "State of the account. 0 - Active; 1 - Locked; 2 - Deleted.")
    private int state;            // 用户状态

    @Column(type = "VARCHAR(64)", nullable = false, comment = "Email address of the user.")
    private String email;

    @Column(type = "VARCHAR(5)", nullable = false, comment = "Phone number country code.")
    private String phoneNumberCountryCode;

    @Column(type = "VARCHAR(16)", nullable = false, comment = "Phone number.")
    private String phoneNumber;

//    @Column(type = "VARCHAR(50)", nullable = false, comment = "Nickname of the user.")
//    private String nickname;       // 昵称，展示用，不要求唯一
}
