package com.svnavigatoru600.url.forum;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.test.category.UnitTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpRequestUtils.class)
@Category(UnitTests.class)
public class ContributionsUrlPartsTest {

    @Test
    public void testGetAbsoluteContributionUrl() throws Exception {
        // Prepares an input.
        final ForumThread thread = new ForumThread();
        final int threadId = 123;
        thread.setId(threadId);
        final ForumContribution contribution = new ForumContribution();
        contribution.setThread(thread);
        contribution.setText("");
        final int contributionId = 123456;
        contribution.setId(contributionId);

        PowerMockito.mockStatic(HttpRequestUtils.class);
        when(HttpRequestUtils.getContextHomeDirectory(any(HttpServletRequest.class))).thenReturn("svnavigatoru600.com");

        // Calls the tested method.
        final String url = ContributionsUrlParts.getAbsoluteContributionUrl(contribution, null);

        // Checks the output.
        assertEquals("svnavigatoru600.com/forum/temata/existujici/123/prispevky/#contribution_123456", url);
    }

    @Test
    public void testGetRelativeContributionUrlAfterCreation() throws Exception {
        // Prepares an input.
        final ForumThread thread = new ForumThread();
        final int threadId = 123;
        thread.setId(threadId);
        final ForumContribution contribution = new ForumContribution();
        contribution.setThread(thread);
        contribution.setText("");
        final int contributionId = 123456;
        contribution.setId(contributionId);

        // Calls the tested method.
        final String url = ContributionsUrlParts.getRelativeContributionUrlAfterCreation(contribution);

        // Checks the output.
        assertEquals("/forum/temata/existujici/123/prispevky/vytvoreno/#contribution_123456", url);
    }
}
