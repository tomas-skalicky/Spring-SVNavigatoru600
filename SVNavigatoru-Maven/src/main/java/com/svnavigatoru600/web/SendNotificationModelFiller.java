package com.svnavigatoru600.web;

import javax.servlet.http.HttpServletRequest;

import org.jpatterns.gof.StrategyPattern;
import org.jpatterns.gof.StrategyPattern.Strategy;
import org.springframework.context.MessageSource;

import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * Contains methods for filling up the {@link com.svnavigatoru600.viewmodel.SendNotification SendNotification}
 * attribute of controller's view model (= command).
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@StrategyPattern(participants = { SendNotificationModelFiller.class, SendNotificationNewModelFiller.class,
        SendNotificationEditModelFiller.class })
@Strategy
public interface SendNotificationModelFiller {

    /**
     * Populates the {@link SendNotificationViewModel#getSendNotification() sendNotification} attribute of the
     * given {@link SendNotificationViewModel view model} (= command).
     * <p>
     * NOTE: Called just during an initialization of form.
     */
    void populateSendNotificationInInitForm(SendNotificationViewModel command, HttpServletRequest request,
            MessageSource messageSource);

    /**
     * Populates the {@link SendNotificationViewModel#getSendNotification() sendNotification} attribute of the
     * given {@link SendNotificationViewModel view model} (= command).
     * <p>
     * NOTE: Called just during a submit of form.
     * <p>
     * It populates especially a title of notification checkbox since this stuff is not passed by POST. The
     * title is necessary when a validator of form data fails.
     */
    void populateSendNotificationInSubmitForm(SendNotificationViewModel command, HttpServletRequest request,
            MessageSource messageSource);
}
