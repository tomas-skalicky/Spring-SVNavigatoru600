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
import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.test.category.ControllerIntegrationTests;
import com.svnavigatoru600.url.users.UserAdministrationUrlParts;
import com.svnavigatoru600.viewmodel.users.validator.NewUserValidator;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(ControllerIntegrationTests.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/applicationContext-business.xml" })
public final class NewUserControllerTest {

    private TestContextManager testContextManager;

    private MockMvc mockMvc;

    private NewUserController controller;

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

    @Mock
    private AuthorityService authorityService;

    @Before
    public void setUpContext() throws Exception {
        // This is where the magic happens. We actually do "by hand" what the
        // spring runner would do for us.
        this.testContextManager = new TestContextManager(this.getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Before
    public void initMockMvc() {
        initMocks();

        this.controller = new NewUserController();
        mockControllerModelAttributes(this.controller);
        mockNewUserControllerServices(this.controller);

        this.mockMvc = standaloneSetup(this.controller).build();
    }

    private void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void mockControllerModelAttributes(AbstractPrivateSectionMetaController controller) {
        controller.setCalendarEventService(this.calendarEventService);
        controller.setContributionService(this.contributionService);
        controller.setUserService(this.userService);
    }

    private void mockNewUserControllerServices(NewUserController controller) {
        controller.setMessageSource(this.messageSource);
        controller.setValidator(this.validator);
        controller.setAuthorityService(this.authorityService);
    }

    @Test
    public void testInitForm() throws Exception {

        // Mocks calls to business logic
        when(this.userService.getLoggerUser()).thenReturn(null);

        // @formatter:off
        this.mockMvc.perform(get(UserAdministrationUrlParts.NEW_URL)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("newUser"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attributeExists("userCommand"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(this.userService).getLoggerUser();
    }
}
