package stark.coderaider.titan.gate.core.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import stark.coderaider.titan.gate.core.domain.dtos.UserPrincipal;

public class UserContextService
{
    private static final UserPrincipal ANONYMOUS_USER;

    static
    {
        ANONYMOUS_USER = new UserPrincipal();
        ANONYMOUS_USER.setUsername("anonymous");
        ANONYMOUS_USER.setPassword("anonymous");
        ANONYMOUS_USER.setId(-1);
    }

    private UserContextService()
    {
    }

    public static void setAuthentication(UsernamePasswordAuthenticationToken authenticationToken)
    {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Returns the currently logged-in user or anonymous user if there is no login state.
     * @return The currently logged-in user or anonymous user if there is no login state.
     */
    public static UserPrincipal getCurrentUser()
    {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal user)
            return user;

        return ANONYMOUS_USER;
    }

    public static Long getCurrentUserId()
    {
        return getCurrentUser().getId();
    }

    public static String getCurrentUsername()
    {
        return getCurrentUser().getUsername();
    }
}
