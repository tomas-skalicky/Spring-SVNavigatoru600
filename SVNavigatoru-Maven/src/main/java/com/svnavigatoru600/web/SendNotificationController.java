package com.svnavigatoru600.web;

/**
 * Implemented by all controllers which enable the user to send a notification of his action.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public interface SendNotificationController {

    /**
     * Name of GET parameter which holds the current status of the notification checkbox.
     */
    String SEND_NOTIFICATION_GET_PARAMETER = "sendNotification";
}
