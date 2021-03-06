package com.svnavigatoru600.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.web.url.ErrorsUrlParts;
import com.svnavigatoru600.web.url.users.UserAccountUrlParts;
import com.svnavigatoru600.web.url.users.UserAdministrationUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * Redirects according to the authorizations of the user who has logged in right now.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        User user = (User) authentication.getPrincipal();
        if (user.canSeeNews()) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        // The following line copied from the implementation of
        // the SavedRequestAwareAuthenticationSuccessHandler class.
        clearAuthenticationAttributes(request);

        String targetUrl;
        if (user.canSeeUsers()) {
            targetUrl = UserAdministrationUrlParts.BASE_URL;
        } else if (user.canSeeHisAccount()) {
            targetUrl = UserAccountUrlParts.BASE_URL;
        } else {
            targetUrl = ErrorsUrlParts.ERROR_403;
        }
        LogFactory.getLog(this.getClass()).debug("Redirecting to the URL: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
