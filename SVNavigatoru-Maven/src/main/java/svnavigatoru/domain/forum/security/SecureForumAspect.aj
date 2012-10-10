package svnavigatoru.domain.forum.security;

public aspect SecureForumAspect {

	declare parents : svnavigatoru.domain.forum.* implements SecureForum;

	private transient boolean SecureForum.editable = false;
	private transient boolean SecureForum.deletable = false;

	public boolean SecureForum.isEditable() {
		return this.editable;
	}

	public void SecureForum.setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean SecureForum.isDeletable() {
		return this.deletable;
	}

	public void SecureForum.setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
}
