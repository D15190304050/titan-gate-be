package stark.coderaider.titan.gate.core.redis;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import stark.dataworks.boot.autoconfig.web.LogRedisKeys;

@Component
@NoArgsConstructor
public class RedisKeyManager
{
    public static String getUserIdKey(long userId)
    {
        return RedisKeyPrefixes.USER + userId;
    }

    public static String getUserRolesKey(long userId, String systemCode)
    {
        return RedisKeyPrefixes.USER_ROLES + userId + "_" + systemCode;
    }
}
