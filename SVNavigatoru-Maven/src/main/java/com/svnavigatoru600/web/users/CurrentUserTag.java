package com.svnavigatoru600.web.users;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.svnavigatoru600.service.util.UserUtils;

public class CurrentUserTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private boolean checkIfLogged = false;
    private boolean checkIfNotLogged = false;

    @Override
    public int doStartTag() throws JspException {
        if (isNotLoggedThough()) {
            return SKIP_BODY;
        }
        if (isLoggedThough()) {
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }

    private boolean isNotLoggedThough() {
        return this.checkIfLogged && !UserUtils.isLogged();
    }

    private boolean isLoggedThough() {
        return this.checkIfNotLogged && UserUtils.isLogged();
    }

    protected boolean isCheckIfLogged() {
        return this.checkIfLogged;
    }

    public void setCheckIfLogged(boolean checkIfLogged) {
        this.checkIfLogged = checkIfLogged;
    }

    protected boolean isCheckIfNotLogged() {
        return this.checkIfNotLogged;
    }

    public void setCheckIfNotLogged(boolean checkIfNotLogged) {
        this.checkIfNotLogged = checkIfNotLogged;
    }
}
