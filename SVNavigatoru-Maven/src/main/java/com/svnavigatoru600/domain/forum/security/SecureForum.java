package com.svnavigatoru600.domain.forum.security;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface SecureForum {

    boolean isEditable();

    void setEditable(boolean editable);

    boolean isDeletable();

    void setDeletable(boolean deletable);
}
