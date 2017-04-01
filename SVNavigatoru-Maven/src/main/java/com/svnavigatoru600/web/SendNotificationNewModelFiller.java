package com.svnavigatoru600.web;

import javax.servlet.http.HttpServletRequest;

import org.jpatterns.gof.StrategyPattern;
import org.jpatterns.gof.StrategyPattern.ConcreteStrategy;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.viewmodel.NewControllerSendNotification;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * Contains methods for filling up the {@link com.svnavigatoru600.viewmodel.SendNotification SendNotification} attribute
 * of view model of <code>New*Controller</code> controllers.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@StrategyPattern(participants = { SendNotificationModelFiller.class, SendNotificationNewModelFiller.class,
        SendNotificationEditModelFiller.class })
@ConcreteStrategy
@Service
public class SendNotificationNewModelFiller implements SendNotificationModelFiller {

    @Override
    public void populateSendNotificationInInitForm(SendNotificationViewModel command, HttpServletRequest request,
            MessageSource messageSource) {
        SendNotification sendNotification = new NewControllerSendNotification(request, messageSource);
        command.setSendNotification(sendNotification);
    }

    @Override
    public void populateSendNotificationInSubmitForm(SendNotificationViewModel command, HttpServletRequest request,
            MessageSource messageSource) {
        command.getSendNotification()
                .setCheckboxTitle(NewControllerSendNotification.getCheckboxTitle(request, messageSource));
    }
}
