package stark.coderaider.titan.gate.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import stark.coderaider.titan.gate.core.config.security.*;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.coderaider.titan.gate.core.services.DaoUserDetailService;
import stark.coderaider.titan.gate.core.services.JwtService;
import stark.dataworks.basic.data.redis.RedisQuickOperation;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
    @Autowired
    private LoginSuccessJsonHandler loginSuccessJsonHandler;

    @Autowired
    private LoginFailureJsonHandler loginFailureJsonHandler;

    @Autowired
    private LogoutSuccessJsonHandler logoutSuccessJsonHandler;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordLoginFilter usernamePasswordLoginFilter() throws Exception
    {
        UsernamePasswordLoginFilter loginFilter = new UsernamePasswordLoginFilter();
        loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        loginFilter.setAuthenticationSuccessHandler(loginSuccessJsonHandler); // Handler for authentication success.
        loginFilter.setAuthenticationFailureHandler(loginFailureJsonHandler); // Handler for authentication failure.
        loginFilter.setFilterProcessesUrl(SecurityConstants.DEFAULT_LOGIN_URI);
        return loginFilter;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, TokenLoginFilter tokenLoginFilter) throws Exception
    {
        http.authorizeHttpRequests(request ->
            {
                request.requestMatchers(SecurityConstants.NON_AUTHENTICATE_URIS).permitAll();
                request.anyRequest().authenticated();
            })
            .exceptionHandling(customizer ->
            {
                customizer.authenticationEntryPoint(new UnauthorizedEntryPoint());
                customizer.accessDeniedHandler(new NoPermissionHandler());
            })
            .formLogin(customizer ->
            {
                customizer.successHandler(loginSuccessJsonHandler);
                customizer.failureHandler(loginFailureJsonHandler);
            })
            .logout(customizer ->
            {
                customizer.logoutUrl(SecurityConstants.DEFAULT_LOGOUT_URI);
                customizer.logoutSuccessHandler(logoutSuccessJsonHandler);
            })
            .cors(customizer -> customizer.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(usernamePasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}