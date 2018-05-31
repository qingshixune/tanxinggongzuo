package cn.gov.zunyi.video.druid;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@EnableTransactionManagement
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidAutoConfiguration {

	@Autowired
	private DruidProperties properties;

	@Bean
	public DataSource dataSource() {
		System.out.println("注入druid的dataSource配置");
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(properties.getUrl());
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		if (properties.getInitialSize() > 0) {
			dataSource.setInitialSize(properties.getInitialSize());
		}
		if (properties.getMinIdle() > 0) {
			dataSource.setMinIdle(properties.getMinIdle());
		}
		if (properties.getMaxActive() > 0) {
			dataSource.setMaxActive(properties.getMaxActive());
		}
		if (properties.getTimeBetweenEvictionRunsMillis() > 0L) {
			dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
		}
		if (properties.getMinEvictableIdleTimeMillis() > 0L) {
			dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
		}

		dataSource.setTestWhileIdle(properties.isTestWhileIdle());
		dataSource.setTestOnBorrow(properties.isTestOnBorrow());
		dataSource.setTestOnReturn(properties.isTestOnReturn());
		dataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
		try {
			dataSource.setFilters("stat,wall");
			dataSource.init();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return dataSource;
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(new StatViewServlet());
		servletRegistrationBean.addUrlMappings("/druid/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		// initParameters.put("loginUsername", "admin");// 用户名
		// initParameters.put("loginPassword", "admin");// 密码
		initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
		initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
		// initParameters.put("deny", "192.168.20.38");// IP黑名单
		// (存在共同时，deny优先于allow)
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	// 按照BeanId来拦截配置 用来bean的监控
	@Bean(value = "druid-stat-interceptor")
	public DruidStatInterceptor DruidStatInterceptor() {
		return new DruidStatInterceptor();
	}

	@Bean
	public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
		BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
		beanNameAutoProxyCreator.setProxyTargetClass(true);
		// 设置要监控的bean的id
		beanNameAutoProxyCreator.setBeanNames("roleMapper");
		beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
		return beanNameAutoProxyCreator;
	}

}
