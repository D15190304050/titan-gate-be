package stark.coderaider.titan.gate.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.domain.dtos.UserDetailsImpl;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.dataworks.basic.data.redis.RedisQuickOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class TitanGateRedisOperation
{
    @Autowired
    private RedisQuickOperation redisQuickOperation;

    public void cacheUser(UserDetailsImpl user)
    {
        long userId = user.getId();
        String userIdKey = RedisKeyManager.getUserIdKey(userId);
        redisQuickOperation.set(userIdKey, user, JwtService.TOKEN_EXPIRATION_IN_DAYS, TimeUnit.DAYS);
    }

    public void cacheUserRoles(long userId, String systemCode, Set<String> roles, long expireMinutes)
    {
        String key = RedisKeyManager.getUserRolesKey(userId, systemCode);
        String value = String.join(",", roles);
        redisQuickOperation.set(key, value, expireMinutes, TimeUnit.MINUTES);
    }

    public Set<String> getCachedUserRoles(long userId, String systemCode)
    {
        String key = RedisKeyManager.getUserRolesKey(userId, systemCode);
        String value = redisQuickOperation.get(key, String.class);
        if (value == null)
        {
            return null;
        }
        if (value.isEmpty())
        {
            return Collections.emptySet();
        }
        String[] parts = value.split(",");
        Set<String> roles = new HashSet<>();
        for (String part : parts)
        {
            if (!part.isEmpty())
            {
                roles.add(part);
            }
        }
        return roles;
    }

    public void removeCachedUserRoles(long userId, String systemCode)
    {
        String key = RedisKeyManager.getUserRolesKey(userId, systemCode);
        redisQuickOperation.delete(key);
    }
}
