package com.svnavigatoru600.domain.forum.security;

public interface SecureForum {

    public boolean isEditable();

    public void setEditable(boolean editable);

    public boolean isDeletable();

    public void setDeletable(boolean deletable);
}
