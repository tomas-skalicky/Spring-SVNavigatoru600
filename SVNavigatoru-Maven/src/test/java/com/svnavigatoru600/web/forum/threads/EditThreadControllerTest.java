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

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.test.category.ControllerIntegrationTests;
import com.svnavigatoru600.test.web.forum.threads.TestConfigForEditThreadControllerTest;
import com.svnavigatoru600.web.url.forum.ThreadsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(ControllerIntegrationTests.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfigForEditThreadControllerTest.class)
public final class EditThreadControllerTest {

    private TestContextManager testContextManager;

    private MockMvc mockMvc;

    @Inject
    private EditThreadController controller;

    @Inject
    private ThreadService threadServiceMock;

    @Before
    public void setUpContext() throws Exception {
        // This is where the magic happens. We actually do "by hand" what the
        // spring runner would do for us.
        testContextManager = new TestContextManager(this.getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Before
    public void initMockMvc() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void testInitForm() throws Exception {
        final int threadId = 211;

        // Mocks calls to business logic
        doNothing().when(threadServiceMock).canEdit(threadId);

        final ForumThread thread = new ForumThread();
        thread.setId(threadId);
        when(threadServiceMock.findById(threadId)).thenReturn(thread);

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
        verify(threadServiceMock).canEdit(threadId);
        verify(threadServiceMock).findById(threadId);
    }

    @Test
    public void testProcessSubmittedForm() throws Exception {
        final int threadId = 9393931;

        // Mocks calls to business logic
        doNothing().when(threadServiceMock).canEdit(threadId);

        final ForumThread thread = new ForumThread();
        thread.setId(threadId);
        when(threadServiceMock.findById(threadId)).thenReturn(thread);
        doNothing().when(threadServiceMock).update(any(ForumThread.class), any(ForumThread.class));

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
        verify(threadServiceMock).canEdit(threadId);
        verify(threadServiceMock).findById(threadId);
        verify(threadServiceMock).update(any(ForumThread.class), any(ForumThread.class));
    }
}
