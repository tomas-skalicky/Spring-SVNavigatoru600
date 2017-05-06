package com.svnavigatoru600.test.web.users.administration.threads;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.users.validator.NewUserValidator;
import com.svnavigatoru600.web.users.administration.NewUserController;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
public class TestConfigForNewUserControllerTest {

    @Bean
    NewUserController newUserController() {
        return new NewUserController();
    }

    @Bean
    AuthorityService authorityService() {
        return mock(AuthorityService.class);
    }

    @Bean
    NewUserValidator newUserValidator() {
        return mock(NewUserValidator.class);
    }

    @Bean
    UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    CalendarEventService calendarEventService() {
        return mock(CalendarEventService.class);
    }

    @Bean
    ContributionService contributionService() {
        return mock(ContributionService.class);
    }

    @Bean
    AuthorityDao authorityDao() {
        return null;
    }

}
