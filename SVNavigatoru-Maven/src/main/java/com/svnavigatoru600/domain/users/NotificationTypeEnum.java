package com.svnavigatoru600.domain.users;

import org.jpatterns.gof.VisitorPattern;
import org.jpatterns.gof.VisitorPattern.Visitor;

/**
 * All possible types of notification settings in the application.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum NotificationTypeEnum {

    // @formatter:off
    //                 titleLocalizationCode
    IN_NEWS           ("notifications.in-news")              { @Override public void accept(final NotificationTypeVisitor visitor) { visitor.visitInNews(); } },
    IN_EVENTS         ("notifications.in-calendar-events")   { @Override public void accept(final NotificationTypeVisitor visitor) { visitor.visitInEvents(); } },
    IN_FORUM          ("notifications.in-forum")             { @Override public void accept(final NotificationTypeVisitor visitor) { visitor.visitInForum(); } },
    IN_OTHER_DOCUMENTS("notifications.in-other-documents")   { @Override public void accept(final NotificationTypeVisitor visitor) { visitor.visitInOtherDocuments(); } },
    IN_OTHER_SECTIONS ("notifications.in-other-sections")    { @Override public void accept(final NotificationTypeVisitor visitor) { visitor.visitInOtherSections(); } },
    ;
    // @formatter:on

    private final String titleLocalizationCode;

    private NotificationTypeEnum(final String titleLocalizationCode) {
        this.titleLocalizationCode = titleLocalizationCode;
    }

    /**
     * Gets the localization code of the title of this {@link NotificationTypeEnum}. Values which correspond to this
     * code are stored in <code>messages*.properties</code> files.
     */
    public String getTitleLocalizationCode() {
        return titleLocalizationCode;
    }

    /**
     * This getter is necessary for Spring Expression Language (SpEL).
     *
     * @return The same value as {@link #ordinal()}.
     */
    public long getOrdinal() {
        return ordinal();
    }

    public abstract void accept(NotificationTypeVisitor visitor);

    /**
     * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
     */
    @VisitorPattern
    @Visitor
    public interface NotificationTypeVisitor {
        void visitInNews();

        void visitInEvents();

        void visitInForum();

        void visitInOtherDocuments();

        void visitInOtherSections();
    }

}
