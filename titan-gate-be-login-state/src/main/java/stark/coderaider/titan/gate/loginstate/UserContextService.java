package stark.coderaider.titan.gate.loginstate;

import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Holds the current request's user context.
 * <p>
 * Each HTTP request will have its own instance of this bean.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContextService
{
    @Setter
    private UserInfo currentUser;

    private static final UserInfo ANONYMOUS_USER;

    static
    {
        ANONYMOUS_USER = new UserInfo();
        ANONYMOUS_USER.setId(-1L);
        ANONYMOUS_USER.setUsername("anonymous");
        ANONYMOUS_USER.setNickname("anonymous");
    }

    public UserInfo getCurrentUser()
    {
        return currentUser != null ? currentUser : ANONYMOUS_USER;
    }

    public Long getCurrentUserId()
    {
        return getCurrentUser().getId();
    }

    public String getCurrentUsername()
    {
        return getCurrentUser().getUsername();
    }
}
