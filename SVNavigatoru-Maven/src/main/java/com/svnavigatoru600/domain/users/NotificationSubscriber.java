package com.svnavigatoru600.domain.users;

import org.jpatterns.gof.VisitorPattern;
import org.jpatterns.gof.VisitorPattern.ConcreteVisitor;

import com.svnavigatoru600.domain.users.NotificationType.NotificationTypeVisitor;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@VisitorPattern(participants = { NotificationTypeVisitor.class, NotificationSubscriber.class })
@ConcreteVisitor
public class NotificationSubscriber implements NotificationTypeVisitor {

    /**
     * Instance of {@link User} of which settings of subscriptions are changed.
     */
    private final User user;
    /**
     * The new setting of a certain subscription.
     */
    private final boolean newIsSubscribed;

    public NotificationSubscriber(User user, boolean newIsSubscribed) {
        this.user = user;
        this.newIsSubscribed = newIsSubscribed;
    }

    @Override
    public void visitInNews() {
        this.user.setSubscribedToNews(this.newIsSubscribed);
    }

    @Override
    public void visitInEvents() {
        this.user.setSubscribedToEvents(this.newIsSubscribed);
    }

    @Override
    public void visitInForum() {
        this.user.setSubscribedToForum(this.newIsSubscribed);
    }

    @Override
    public void visitInOtherDocuments() {
        this.user.setSubscribedToOtherDocuments(this.newIsSubscribed);
    }

    @Override
    public void visitInOtherSections() {
        this.user.setSubscribedToOtherSections(this.newIsSubscribed);
    }
}