package com.svnavigatoru600.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.util.Localization;

/**
 * Ancestor of all {@link Service Services} which provide sending of emails for any purpose.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractEmailService {

    private static final String EMAIL_TEXT_ADDRESSING_CODE = "email.text.addressing";
    private static final String EMAIL_TEXT_ADMIN_SIGNATURE_CODE = "email.text.admin-signature";

    /**
     * Gets localized addressing of the {@link User recipient}.
     * 
     * @param recipient
     *            Addressed user
     */
    protected String getLocalizedRecipientAddressing(User recipient, HttpServletRequest request,
            MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractEmailService.EMAIL_TEXT_ADDRESSING_CODE, recipient.getLastName());
    }

    /**
     * Gets localized signature of administrator of these web pages.
     */
    protected String getLocalizedAdminSignature(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractEmailService.EMAIL_TEXT_ADMIN_SIGNATURE_CODE, Configuration.DOMAIN);
    }
}
