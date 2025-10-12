package stark.coderaider.titan.gate.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.domain.dtos.UserDetailsImpl;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.dataworks.basic.data.redis.RedisQuickOperation;

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
}
