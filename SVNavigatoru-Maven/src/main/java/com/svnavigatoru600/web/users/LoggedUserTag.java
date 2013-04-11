package com.svnavigatoru600.web.users;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.svnavigatoru600.service.util.UserUtils;

public class LoggedUserTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private boolean checkIfCanEditNews = false;
    private boolean checkIfCanEditEvents = false;
    private boolean checkIfCanEditOtherDocumentRecords = false;
    private boolean checkIfCanEditSessionRecords = false;
    private boolean checkIfCanEditWysiwygSites = false;
    private boolean checkIfCanSeeEvents = false;
    private boolean checkIfCanSeeContributions = false;
    private boolean checkIfCanSeeHomepage = false;
    private boolean checkIfCanSeeNews = false;
    private boolean checkIfCanSeeUserAdministration = false;
    private boolean checkIfCanSeeHisAccount = false;

    @Override
    public int doStartTag() throws JspException {
        if (cannotEditThough()) {
            return SKIP_BODY;
        }
        if (cannotSeeThough()) {
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }

    private boolean cannotEditThough() {
        if (cannotEditNewsThough()) {
            return true;
        }
        if (cannotEditEventsThough()) {
            return true;
        }
        if (cannotEditOtherDocumentRecordsThough()) {
            return true;
        }
        if (cannotEditSessionRecordsThough()) {
            return true;
        }
        if (cannotEditWysiwygSitesThough()) {
            return true;
        }
        return false;
    }

    private boolean cannotEditNewsThough() {
        return this.checkIfCanEditNews && !UserUtils.getLoggedUser().canEditNews();
    }

    private boolean cannotEditEventsThough() {
        return this.checkIfCanEditEvents && !UserUtils.getLoggedUser().canEditNews();
    }

    private boolean cannotEditOtherDocumentRecordsThough() {
        return this.checkIfCanEditOtherDocumentRecords && !UserUtils.getLoggedUser().canEditNews();
    }

    private boolean cannotEditSessionRecordsThough() {
        return this.checkIfCanEditSessionRecords && !UserUtils.getLoggedUser().canEditNews();
    }

    private boolean cannotEditWysiwygSitesThough() {
        return this.checkIfCanEditWysiwygSites && !UserUtils.getLoggedUser().canEditNews();
    }

    private boolean cannotSeeThough() {
        if (cannotSeeEventsThough()) {
            return true;
        }
        if (cannotSeeContributionsThough()) {
            return true;
        }
        if (cannotSeeHomepageThough()) {
            return true;
        }
        if (cannotSeeNewsThough()) {
            return true;
        }
        if (cannotSeeUserAdministrationThough()) {
            return true;
        }
        if (cannotSeeHisAccountThough()) {
            return true;
        }
        return false;
    }

    private boolean cannotSeeEventsThough() {
        return this.checkIfCanSeeEvents && !UserUtils.getLoggedUser().canSeeNews();
    }

    private boolean cannotSeeContributionsThough() {
        return this.checkIfCanSeeContributions && !UserUtils.getLoggedUser().canSeeNews();
    }

    private boolean cannotSeeHomepageThough() {
        return this.checkIfCanSeeHomepage && !UserUtils.getLoggedUser().canSeeNews();
    }

    private boolean cannotSeeNewsThough() {
        return this.checkIfCanSeeNews && !UserUtils.getLoggedUser().canSeeNews();
    }

    private boolean cannotSeeUserAdministrationThough() {
        return this.checkIfCanSeeUserAdministration && !UserUtils.getLoggedUser().canSeeUsers();
    }

    private boolean cannotSeeHisAccountThough() {
        return this.checkIfCanSeeHisAccount && !UserUtils.getLoggedUser().canSeeHisAccount();
    }

    protected boolean isCheckIfCanEditNews() {
        return this.checkIfCanEditNews;
    }

    public void setCheckIfCanEditNews(boolean checkIfCanEditNews) {
        this.checkIfCanEditNews = checkIfCanEditNews;
    }

    protected boolean isCheckIfCanEditEvents() {
        return this.checkIfCanEditEvents;
    }

    public void setCheckIfCanEditEvents(boolean checkIfCanEditEvents) {
        this.checkIfCanEditEvents = checkIfCanEditEvents;
    }

    protected boolean isCheckIfCanEditOtherDocumentRecords() {
        return this.checkIfCanEditOtherDocumentRecords;
    }

    public void setCheckIfCanEditOtherDocumentRecords(boolean checkIfCanEditOtherDocumentRecords) {
        this.checkIfCanEditOtherDocumentRecords = checkIfCanEditOtherDocumentRecords;
    }

    protected boolean isCheckIfCanEditSessionRecords() {
        return this.checkIfCanEditSessionRecords;
    }

    public void setCheckIfCanEditSessionRecords(boolean checkIfCanEditSessionRecords) {
        this.checkIfCanEditSessionRecords = checkIfCanEditSessionRecords;
    }

    protected boolean isCheckIfCanEditWysiwygSites() {
        return this.checkIfCanEditWysiwygSites;
    }

    public void setCheckIfCanEditWysiwygSites(boolean checkIfCanEditWysiwygSites) {
        this.checkIfCanEditWysiwygSites = checkIfCanEditWysiwygSites;
    }

    protected boolean isCheckIfCanSeeEvents() {
        return this.checkIfCanSeeEvents;
    }

    public void setCheckIfCanSeeEvents(boolean checkIfCanSeeEvents) {
        this.checkIfCanSeeEvents = checkIfCanSeeEvents;
    }

    protected boolean isCheckIfCanSeeContributions() {
        return this.checkIfCanSeeContributions;
    }

    public void setCheckIfCanSeeContributions(boolean checkIfCanSeeContributions) {
        this.checkIfCanSeeContributions = checkIfCanSeeContributions;
    }

    protected boolean isCheckIfCanSeeHomepage() {
        return this.checkIfCanSeeHomepage;
    }

    public void setCheckIfCanSeeHomepage(boolean checkIfCanSeeHomepage) {
        this.checkIfCanSeeHomepage = checkIfCanSeeHomepage;
    }

    public boolean isCheckIfCanSeeNews() {
        return this.checkIfCanSeeNews;
    }

    public void setCheckIfCanSeeNews(boolean checkIfCanSeeNews) {
        this.checkIfCanSeeNews = checkIfCanSeeNews;
    }

    protected boolean isCheckIfCanSeeUserAdministration() {
        return this.checkIfCanSeeUserAdministration;
    }

    public void setCheckIfCanSeeUserAdministration(boolean checkIfCanSeeUserAdministration) {
        this.checkIfCanSeeUserAdministration = checkIfCanSeeUserAdministration;
    }

    public boolean isCheckIfCanSeeHisAccount() {
        return this.checkIfCanSeeHisAccount;
    }

    public void setCheckIfCanSeeHisAccount(boolean checkIfCanSeeHisAccount) {
        this.checkIfCanSeeHisAccount = checkIfCanSeeHisAccount;
    }
}
