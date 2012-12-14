package com.svnavigatoru600.selenium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * {@link org.springframework.context.ApplicationContext Application context} for Selenium tests. It creates
 * necessary {@link Bean beans}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Configuration
@PropertySource("classpath:selenium_environment.properties")
public class SeleniumAppConfig {

    /**
     * Used together with the {@link PropertySource} annotation of this class.
     */
    @Autowired
    private Environment environment;

    /**
     * Gets the test user which used in Selenium tests to sign-in. It means that his credentials are stored in
     * the DB.
     * 
     * @return The test user
     */
    @Bean(name = "testUser")
    public TestUser getTestUser() {
        final TestUser user = new TestUser();
        user.setUsername(this.environment.getProperty("testUser.username"));
        user.setPassword(this.environment.getProperty("testUser.password"));
        return user;
    }

    /**
     * Gets information about server where the application is deployed to.
     * 
     * @return The deployment {@link Server}
     */
    @Bean(name = "deployServer")
    public Server getDeployServer() {
        final Server server = new Server();
        server.setHost(this.environment.getProperty("deploy.host"));
        server.setPort(Integer.valueOf(this.environment.getProperty("deploy.port")).intValue());
        server.setPath(this.environment.getProperty("deploy.path"));
        return server;
    }

    /**
     * Gets information about server where the Selenium server (Selenium hub respectively) is running.
     * 
     * @return The Selenium {@link Server hub}
     */
    @Bean(name = "seleniumServer")
    public Server getSeleniumServer() {
        final Server server = new Server();
        server.setHost(this.environment.getProperty("seleniumHub.host"));
        server.setPort(Integer.valueOf(this.environment.getProperty("seleniumHub.port")).intValue());
        server.setPath(this.environment.getProperty("seleniumHub.path"));
        return server;
    }
}
