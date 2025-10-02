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
public class UserOauth2 extends EntityBase
{
    @Column(nullable = false, unique = true, comment = "用户 ID")
    private long userId;         // 本地系统 User 的 ID

    @Column(type = "VARCHAR(64)", nullable = false, comment = "外部 OAuth2 提供方")
    private String provider;     // 外部 OAuth2 提供方 (github, google, wechat, apple...)

    @Column(type = "VARCHAR(64)", nullable = false, comment = "外部 OAuth2 提供方账号唯一标识")
    private String providerUid;  // 外部账号的唯一标识（如 sub、id）

    @Column(type = "VARCHAR(500)", comment = "Access token of the provider.")
    private String accessToken;  // 获取到的 access_token（可选，看你是否需要）

    @Column(type = "VARCHAR(500)", comment = "Refresh token of the provider.")
    private String refreshToken; // 获取到的 refresh_token（可选，看你是否需要）

    @Column(comment = "Access token expiration time in seconds.")
    private long expiresIn;      // access_token 过期时间戳（可选）
}
