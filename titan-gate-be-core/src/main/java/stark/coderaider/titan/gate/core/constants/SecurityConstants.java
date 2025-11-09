package stark.coderaider.titan.gate.core.constants;

import org.springframework.http.HttpHeaders;

import java.util.List;

public class SecurityConstants
{
    private SecurityConstants()
    {
    }

    public static final String USER_ID = "user_id";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String USERNAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    public static final String TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String LOGIN_SUCCESS = "Login success.";
    public static final String LOGOUT_SUCCESS = "Logout success.";
    public static final String DEFAULT_LOGIN_URI = "/login";
    public static final String DEFAULT_LOGOUT_URI = "/logout";

    public static final String SSO_COOKIE_NAME = "titan_gate_login";
    public static final String REDIRECT_URL = "redirect_url";

    public static final List<String> NON_AUTHENTICATION_URIS = List.of(
        DEFAULT_LOGIN_URI,
        "/connection/**",
        "/auth/**",
        "/oauth2/**",
        "/sso/validate-token",
        "/public/**"
    );
}
