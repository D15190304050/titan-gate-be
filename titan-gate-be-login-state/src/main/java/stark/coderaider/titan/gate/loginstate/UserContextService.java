package stark.coderaider.titan.gate.loginstate;

public final class UserContextService
{

    private UserContextService()
    {
    }

    private static final UserInfo ANONYMOUS_USER;

    static
    {
        ANONYMOUS_USER = new UserInfo();
        ANONYMOUS_USER.setId(-1L);
        ANONYMOUS_USER.setUsername("anonymous");
        ANONYMOUS_USER.setNickname("anonymous");
    }

    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public static void setCurrentUser(UserInfo user)
    {
        HOLDER.set(user);
    }

    public static UserInfo getCurrentUser()
    {
        UserInfo user = HOLDER.get();
        return user != null ? user : ANONYMOUS_USER;
    }

    public static long getCurrentUserId()
    {
        return getCurrentUser().getId();
    }

    public static String getCurrentUsername()
    {
        return getCurrentUser().getUsername();
    }

    /**
     * 请求结束时必须调用
     */
    public static void clear()
    {
        HOLDER.remove();
    }
}