package com.svnavigatoru600.web.users.administration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.test.category.ControllerIntegrationTests;
import com.svnavigatoru600.url.users.UserAdministrationUrlParts;
import com.svnavigatoru600.viewmodel.users.validator.NewUserValidator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(ControllerIntegrationTests.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext-business.xml" })
public final class NewUserControllerTest {

    private TestContextManager testContextManager;

    @Before
    public void setUpContext() throws Exception {
        // This is where the magic happens. We actually do "by hand" what the
        // spring runner would do for us.
        this.testContextManager = new TestContextManager(this.getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    private MockMvc mockMvc;
    private NewUserController controller;

    @Before
    public void initMockMvc() {
        this.controller = new NewUserController();
        this.mockMvc = standaloneSetup(this.controller).build();
        initMocks();
    }

    @Mock
    private CalendarEventService calendarEventService;
    @Mock
    private ContributionService contributionService;
    @Mock
    private UserService userService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private NewUserValidator validator;

    private void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.controller.setCalendarEventService(this.calendarEventService);
        this.controller.setContributionService(this.contributionService);
        this.controller.setUserService(this.userService);
        this.controller.setMessageSource(this.messageSource);
        this.controller.setValidator(this.validator);
    }

    @Test
    public void testInitForm() throws Exception {
        when(this.userService.getLoggerUser()).thenReturn(null);

        this.mockMvc.perform(get(UserAdministrationUrlParts.NEW_URL).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk()).andExpect(forwardedUrl("newUser"))
                .andExpect(model().errorCount(0)).andExpect(model().attributeExists("userCommand"))
                .andDo(print());

        verify(this.userService).getLoggerUser();
    }
}
