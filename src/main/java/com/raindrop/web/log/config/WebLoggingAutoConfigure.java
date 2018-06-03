package com.raindrop.web.log.config;

import com.raindrop.web.log.filter.WebLoggingFilter;
import com.raindrop.web.log.properties.WebLoggingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name: com.raindrop.web.log.config.WebLoggingAutoConfig.java
 * @description: WebLog自动装配
 * @author: Wang Liang
 * @create Time: 2018/6/1 21:15
 */
@Configuration
@ConditionalOnClass(WebLoggingFilter.class)
@EnableConfigurationProperties(WebLoggingProperties.class)
@ConditionalOnProperty(prefix = "web.log", value = "enable", havingValue = "true")
public class WebLoggingAutoConfigure {

	private final WebLoggingProperties properties;

	public WebLoggingAutoConfigure(WebLoggingProperties properties) { this.properties = properties; }

	@Bean
	@ConditionalOnMissingBean(WebLoggingFilter.class)
	public FilterRegistrationBean registrationBean() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		// register custom filter
		registration.setFilter(new WebLoggingFilter(properties.getExcludeMappingPath(), properties.getPrintHeader()));
		// add filter rule(path)
		registration.addUrlPatterns(properties.getMappingPath());
		// set register name
		registration.setName("WebLoggingFilter");
		// Automatic registration filter
		boolean isEnable = "true".equals(properties.getEnable()) ? true : false;
		registration.setEnabled(isEnable);
		return registration;
	}

}
