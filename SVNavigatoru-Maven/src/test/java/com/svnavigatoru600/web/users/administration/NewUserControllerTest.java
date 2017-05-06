package com.svnavigatoru600.web.users.administration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.test.category.ControllerIntegrationTests;
import com.svnavigatoru600.test.web.users.administration.threads.TestConfigForNewUserControllerTest;
import com.svnavigatoru600.web.url.users.UserAdministrationUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(ControllerIntegrationTests.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfigForNewUserControllerTest.class)
public final class NewUserControllerTest {

    private TestContextManager testContextManager;

    private MockMvc mockMvc;

    @Inject
    private NewUserController controller;

    @Inject
    private UserService userServiceMock;

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

        // Mocks calls to business logic
        when(userServiceMock.getLoggerUser()).thenReturn(null);

        // @formatter:off
        mockMvc.perform(get(UserAdministrationUrlParts.NEW_URL)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("newUser"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attributeExists("userCommand"))
                .andDo(print());
        // @formatter:on

        // Verifies mocked calls
        verify(userServiceMock).getLoggerUser();
    }
}
