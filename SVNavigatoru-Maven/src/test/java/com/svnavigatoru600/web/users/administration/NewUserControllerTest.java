package com.svnavigatoru600.web.users.administration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.service.users.AuthorityService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class NewUserControllerTest {

    @Mock
    private AuthorityService authorityService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private NewUserController newUserController;

    @Mock
    private HttpServletRequest request;
    @Mock
    private ModelMap model;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitForm() throws Exception {
        when(this.authorityService.getLocalizedRoleTitles(this.request, this.messageSource)).thenReturn(null);

        String view = this.newUserController.initForm(this.request, this.model);

        assertEquals("newUser", view);
    }
}
