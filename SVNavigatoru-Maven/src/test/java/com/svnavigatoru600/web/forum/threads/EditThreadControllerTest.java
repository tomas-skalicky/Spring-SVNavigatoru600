package com.svnavigatoru600.web.forum.threads;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

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

import com.svnavigatoru600.domain.forum.ForumThread;
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
        testContextManager = new TestContextManager(this.getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Before
    public void initMockMvc() {
        initMocks();

        controller = new EditThreadController(threadService, editThreadValidator, messageSource);
        mockControllerModelAttributes(controller);

        mockMvc = standaloneSetup(controller).build();
    }

    private void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void mockControllerModelAttributes(final AbstractPrivateSectionMetaController controller) {
        controller.setCalendarEventService(calendarEventService);
        controller.setContributionService(contributionService);
        controller.setUserService(userService);
    }

    @Test
    public void testInitForm() throws Exception {
        final int threadId = 211;

        // Mocks calls to business logic
        doNothing().when(threadService).canEdit(threadId);

        final ForumThread thread = new ForumThread();
        thread.setId(threadId);
        when(threadService.findById(threadId)).thenReturn(thread);

        // @formatter:off
        mockMvc.perform(get(ThreadsUrlParts.EXISTING_URL + threadId + "/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("editThread"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attributeExists("newEditThreadCommand"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(threadService).canEdit(threadId);
        verify(threadService).findById(threadId);
    }

    @Test
    public void testProcessSubmittedForm() throws Exception {
        final int threadId = 9393931;

        // Mocks calls to business logic
        doNothing().when(threadService).canEdit(threadId);

        final ForumThread thread = new ForumThread();
        thread.setId(threadId);
        when(threadService.findById(threadId)).thenReturn(thread);
        doNothing().when(threadService).update(any(ForumThread.class), any(ForumThread.class));

        // @formatter:off
        mockMvc.perform(post(ThreadsUrlParts.EXISTING_URL + threadId + "/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("page-for-redirection"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute("redirectTo", "/forum/temata/existujici/9393931/ulozeno/"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(threadService).canEdit(threadId);
        verify(threadService).findById(threadId);
        verify(threadService).update(any(ForumThread.class), any(ForumThread.class));
    }
}
