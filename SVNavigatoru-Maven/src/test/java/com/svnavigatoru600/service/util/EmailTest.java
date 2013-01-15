package com.svnavigatoru600.service.util;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.Configuration;
import com.svnavigatoru600.test.category.FastTests;
import com.svnavigatoru600.web.users.EditMyAccountController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(FastTests.class)
public class EmailTest {

    @Test
    public void testGetLinkForUnsubscription() throws Exception {
        User user = new User();
        String username = "username";
        user.setUsername(username);

        // getLinkForUnsubscription method is private!
        @SuppressWarnings("rawtypes")
        Class[] parameterTypes = { NotificationType.class, User.class };
        Method getLinkForUnsubscriptionMethod = Email.class.getDeclaredMethod("getLinkForUnsubscription",
                parameterTypes);
        getLinkForUnsubscriptionMethod.setAccessible(true);

        // Calls the private method.
        NotificationType notificationType = NotificationType.IN_OTHER_DOCUMENTS;
        String resultLink = (String) getLinkForUnsubscriptionMethod.invoke(null, notificationType, user);

        String expectedLink = new StringBuffer(Configuration.DOMAIN).append(EditMyAccountController.BASE_URL)
                .append(username).append("/odhlasit-notifikace/").append(notificationType.ordinal())
                .append("/").toString();
        Assert.assertEquals(expectedLink, resultLink);
    }
}
