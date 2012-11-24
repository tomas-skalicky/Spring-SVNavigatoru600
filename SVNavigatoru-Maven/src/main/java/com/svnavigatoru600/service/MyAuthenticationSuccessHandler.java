package com.svnavigatoru600.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.svnavigatoru600.domain.users.User;


public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Redirects according to the authorizations of the user who has logged in
	 * right now.
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
		this.clearAuthenticationAttributes(request);

		String targetUrl;
		if (user.canSeeUsers()) {
			targetUrl = "/administrace-uzivatelu/";
		} else if (user.canSeeHisAccount()) {
			targetUrl = "/uzivatelsky-ucet/";
		} else {
			targetUrl = "/chyby/403/";
		}
		this.logger.debug("Redirecting to the URL: " + targetUrl);
		this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
