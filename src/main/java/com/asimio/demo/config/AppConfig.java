package com.asimio.demo.config;

import javax.servlet.Filter;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.asimio.demo.tenant.TenantStore;
import com.asimio.demo.web.TenantFilter;
import com.google.common.collect.Lists;

@Configuration
public class AppConfig {

	@Bean
	public Filter tenantFilter() {
		return new TenantFilter();
	}

	@Bean
	public FilterRegistrationBean tenantFilterRegistration() {
		FilterRegistrationBean result = new FilterRegistrationBean();
		result.setFilter(this.tenantFilter());
		result.setUrlPatterns(Lists.newArrayList("/*"));
		result.setName("Tenant Store Filter");
		result.setOrder(1);
		return result;
	}

	@Bean(destroyMethod = "destroy")
	public ThreadLocalTargetSource threadLocalTenantStore() {
		ThreadLocalTargetSource result = new ThreadLocalTargetSource();
		result.setTargetBeanName("tenantStore");
		return result;
	}

	@Primary
	@Bean(name = "proxiedThreadLocalTargetSource")
	public ProxyFactoryBean proxiedThreadLocalTargetSource(ThreadLocalTargetSource threadLocalTargetSource) {
		ProxyFactoryBean result = new ProxyFactoryBean();
		result.setTargetSource(threadLocalTargetSource);
		return result;
	}

	@Bean(name = "tenantStore")
	@Scope(scopeName = "prototype")
	public TenantStore tenantStore() {
		return new TenantStore();
	}

}