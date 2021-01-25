package app.quickcase.logging.spring;

import app.quickcase.logging.filters.RequestIdsSettingFilter;
import app.quickcase.logging.filters.RequestStatusLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class RequestLoggingAutoConfiguration {

    @Bean
    public FilterRegistrationBean requestIdLoggingFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestIdsSettingFilter());
        filterRegistrationBean.setOrder(HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean requestStatusLoggingFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestStatusLoggingFilter());
        filterRegistrationBean.setOrder(HIGHEST_PRECEDENCE + 1);
        return filterRegistrationBean;
    }
}
