package com.svnavigatoru600.web.forum.threads;

import static org.mockito.Matchers.*;
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

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.test.category.ControllerIntegrationTests;
import com.svnavigatoru600.url.forum.ThreadsUrlParts;
import com.svnavigatoru600.viewmodel.forum.threads.validator.EditThreadValidator;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(ControllerIntegrationTests.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/applicationContext-business.xml" })
public final class EditThreadControllerTest {

    private TestContextManager testContextManager;

    private MockMvc mockMvc;

    private EditThreadController controller;

    @Mock
    private ThreadService threadService;

    @Mock
    private EditThreadValidator editThreadValidator;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CalendarEventService calendarEventService;

    @Mock
    private ContributionService contributionService;

    @Mock
    private UserService userService;

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

        this.controller = new EditThreadController(this.threadService, this.editThreadValidator,
                this.messageSource);
        mockControllerModelAttributes(this.controller);

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

    @Test
    public void testInitForm() throws Exception {
        final int threadId = 211;

        // Mocks calls to business logic
        doNothing().when(this.threadService).canEdit(threadId);
        when(this.threadService.findById(threadId)).thenReturn(new Thread.Builder().withId(threadId).build());

        // @formatter:off
        this.mockMvc.perform(get(ThreadsUrlParts.EXISTING_URL + threadId + "/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("editThread"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attributeExists("newEditThreadCommand"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(this.threadService).canEdit(threadId);
        verify(this.threadService).findById(threadId);
    }

    @Test
    public void testProcessSubmittedForm() throws Exception {
        final int threadId = 9393931;

        // Mocks calls to business logic
        doNothing().when(this.threadService).canEdit(threadId);
        when(this.threadService.findById(threadId)).thenReturn(new Thread.Builder().withId(threadId).build());
        doNothing().when(this.threadService).update(any(Thread.class), any(Thread.class));

        // @formatter:off
        this.mockMvc.perform(post(ThreadsUrlParts.EXISTING_URL + threadId + "/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("page-for-redirection"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute("redirectTo", "/forum/temata/existujici/9393931/ulozeno/"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(this.threadService).canEdit(threadId);
        verify(this.threadService).findById(threadId);
        verify(this.threadService).update(any(Thread.class), any(Thread.class));
    }
}
