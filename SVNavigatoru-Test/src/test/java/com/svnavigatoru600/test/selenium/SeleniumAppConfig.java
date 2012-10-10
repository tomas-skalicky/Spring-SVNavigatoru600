package com.svnavigatoru600.test.selenium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * {@link org.springframework.context.ApplicationContext Application context} for Selenium tests. It creates necessary {@link Bean beans}.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Configuration
@PropertySource("classpath:selenium_environment.properties")
public class SeleniumAppConfig {

	/**
	 * Used together with the {@link PropertySource} annotation of this class.
	 */
	@Autowired
	private Environment environment;

	@Bean(name = "testUser")
	public TestUser getTestUser() {
		final TestUser user = new TestUser();
		user.setUsername(this.environment.getProperty("testUser.username"));
		user.setPassword(this.environment.getProperty("testUser.password"));
		return user;
	}

	@Bean(name = "deployServer")
	public DeployServer getDeployServer() {
		final DeployServer server = new DeployServer();
		server.setHost(this.environment.getProperty("deploy.host"));
		server.setPort(Integer.valueOf(this.environment.getProperty("deploy.port")).intValue());
		return server;
	}
}
