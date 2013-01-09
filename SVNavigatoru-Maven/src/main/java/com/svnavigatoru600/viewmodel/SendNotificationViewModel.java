/**
 * 
 */
package com.svnavigatoru600.viewmodel;

/**
 * View models which hold information about {@link SendNotification} checkbox.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public interface SendNotificationViewModel {

    SendNotification getSendNotification();

    void setSendNotification(SendNotification sendNotification);
}
