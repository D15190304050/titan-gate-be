package stark.coderaider.titan.gate.loginstate;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class FilterConfiguration
{
    @Bean
    public FilterRegistrationBean<TitanLoginStateFilter> titanLoginStateFilterRegistration()
    {
        TitanLoginStateFilter titanLoginStateFilter = new TitanLoginStateFilter();
        return registerFilter(titanLoginStateFilter, Ordered.HIGHEST_PRECEDENCE + 2);
    }

    @Bean
    public FilterRegistrationBean<UserContextCleanFilter> userContextCleanFilterRegistration()
    {
        UserContextCleanFilter userContextCleanFilter = new UserContextCleanFilter();
        return registerFilter(userContextCleanFilter, Ordered.HIGHEST_PRECEDENCE + 1);
    }

    public <T extends OncePerRequestFilter> FilterRegistrationBean<T> registerFilter(T filter, int precedence)
    {
        FilterRegistrationBean<T> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(precedence);
        registration.setName(filter.getClass().getSimpleName());
        return registration;
    }
}