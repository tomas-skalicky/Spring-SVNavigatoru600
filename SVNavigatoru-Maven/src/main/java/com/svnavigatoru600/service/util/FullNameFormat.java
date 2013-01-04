package com.svnavigatoru600.service.util;

import org.jpatterns.gof.VisitorPattern;
import org.jpatterns.gof.VisitorPattern.Visitor;

/**
 * Enumeration of all possible formats of the full name of the {@link com.svnavigatoru600.domain.users.User}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum FullNameFormat {

    /**
     * For example: "Tomas Skalicky"
     */
    FIRST_LAST {
        @Override
        public <E> E accept(FullNameFormatVisitor<E> visitor) {
            return visitor.visitFirstLast();
        }
    },

    /**
     * For example: "Skalicky Tomas"
     */
    LAST_FIRST {
        @Override
        public <E> E accept(FullNameFormatVisitor<E> visitor) {
            return visitor.visitLastFirst();
        }
    },

    /**
     * For example: "Skalicky, Tomas"
     */
    LAST_COMMA_FIRST {
        @Override
        public <E> E accept(FullNameFormatVisitor<E> visitor) {
            return visitor.visitLastCommaFirst();
        }
    };

    public abstract <E> E accept(FullNameFormatVisitor<E> visitor);

    /**
     * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
     */
    @VisitorPattern(participants = { FullNameFormatVisitor.class, UserFullNameFormatter.class })
    @Visitor
    public interface FullNameFormatVisitor<E> {
        E visitFirstLast();

        E visitLastFirst();

        E visitLastCommaFirst();
    }
}
