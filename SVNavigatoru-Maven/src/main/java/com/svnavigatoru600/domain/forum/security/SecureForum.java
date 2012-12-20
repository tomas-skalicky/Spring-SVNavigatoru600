package com.svnavigatoru600.domain.forum.security;

public interface SecureForum {

    boolean isEditable();

    void setEditable(boolean editable);

    boolean isDeletable();

    void setDeletable(boolean deletable);
}
