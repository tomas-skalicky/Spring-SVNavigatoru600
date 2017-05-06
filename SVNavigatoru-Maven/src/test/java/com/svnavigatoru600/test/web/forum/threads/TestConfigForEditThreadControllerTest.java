package com.svnavigatoru600.test.web.forum.threads;

import static org.mockito.Mockito.mock;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.forum.ThreadNotificationEmailService;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.forum.threads.validator.EditThreadValidator;
import com.svnavigatoru600.web.forum.threads.EditThreadController;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
public class TestConfigForEditThreadControllerTest {

    @Bean
    EditThreadController editThreadController() {
        return new EditThreadController(threadService(), editThreadValidator(), messageSource());
    }

    @Bean
    ThreadService threadService() {
        return mock(ThreadService.class);
    }

    @Bean
    EditThreadValidator editThreadValidator() {
        return mock(EditThreadValidator.class);
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
    MessageSource messageSource() {
        return null;
    }

    @Bean
    ThreadNotificationEmailService threadNotificationEmailService() {
        return null;
    }

    @Bean
    ThreadDao threadDao() {
        return null;
    }

}
