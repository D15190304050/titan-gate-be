package stark.coderaider.titan.gate.loginstate;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfiguration
{
    @Bean
    public FilterRegistrationBean<TitanLoginStateFilter> titanLoginStateFilterRegistration(TitanLoginStateFilter titanLoginStateFilter)
    {
        FilterRegistrationBean<TitanLoginStateFilter> registration = new FilterRegistrationBean<>();

        // 1. 设置要注册的 Filter 实例
        registration.setFilter(titanLoginStateFilter);

        // 2. 设置 Filter 匹配的 URL 模式（例如，匹配所有请求）
        registration.addUrlPatterns("/*");

        // 3. 设置 Filter 的执行顺序
        // Ordered.HIGHEST_PRECEDENCE 表示最高的优先级，即最先执行
        // 较低的数字表示较高的优先级。如果有很多 Filter，可以按需设置数字
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        // 4. (可选) 设置 Filter 名称
        registration.setName("titanLoginStateFilter");

        return registration;
    }
}