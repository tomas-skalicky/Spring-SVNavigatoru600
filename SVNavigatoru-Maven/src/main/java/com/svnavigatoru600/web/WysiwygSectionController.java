package com.svnavigatoru600.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;

@Controller
public abstract class WysiwygSectionController extends PrivateSectionMetaController {

    protected WysiwygSectionDao sectionDao;
    protected WysiwygSectionName sectionName;
    protected String viewPageView;
    protected String editPageView;
    protected String viewPageAddress;

    /**
     * Constructor.
     */
    @Autowired
    public WysiwygSectionController(WysiwygSectionDao sectionDao) {
        this.logger.debug("The WysiwygSectionController object created.");
        this.sectionDao = sectionDao;
    }

    public String showViewPage(ModelMap model) {
        try {
            model.addAttribute("section", this.sectionDao.findByName(this.sectionName));
        } catch (DataAccessException e) {
            this.logger.error(null, e);
        }
        return this.viewPageView;
    }

    public String showEditPage(ModelMap model) {
        model.addAttribute("wysiwygSectionEditCommand", this.sectionDao.findByName(this.sectionName));
        return this.editPageView;
    }

    @Transactional
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
            BindingResult result, SessionStatus status, ModelMap model) {
        // No validation necessary.

        command.setName(this.sectionName);

        try {
            // Stores the data.
            this.sectionDao.update(command);

            // Clears the command object from the session.
            status.setComplete();
        } catch (DataAccessException e) {
            this.logger.error(null, e);
            result.reject("edit.changes-not-saved-due-to-database-error");
        }
        return this.editPageView;
    }

    @Transactional
    public String saveChangesAndFinishEditing(
            @ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command, BindingResult result,
            SessionStatus status, ModelMap model) {
        // No validation necessary.

        this.saveChanges(command, result, status, model);
        if (result.hasErrors()) {
            return this.editPageView;
        } else {
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, this.viewPageAddress);
            return Configuration.REDIRECTION_PAGE;
        }
    }

    public String cancelChangesAndFinishEditing(ModelMap model) {
        model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, this.viewPageAddress);
        return Configuration.REDIRECTION_PAGE;
    }
}
