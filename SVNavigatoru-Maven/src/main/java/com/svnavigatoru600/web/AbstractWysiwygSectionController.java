package com.svnavigatoru600.web;

import javax.inject.Inject;

import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.service.WysiwygSectionService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractWysiwygSectionController extends AbstractPrivateSectionMetaController {

    private final WysiwygSectionService sectionService;
    private final WysiwygSectionName sectionName;
    private final String viewPageView;
    private final String editPageView;
    private final String viewPageAddress;

    /**
     * Constructor.
     */
    @Inject
    public AbstractWysiwygSectionController(WysiwygSectionService sectionService,
            WysiwygSectionName sectionName, String viewPageView, String editPageView, String viewPageAddress) {
        LogFactory.getLog(this.getClass()).debug("The WysiwygSectionController object created.");
        this.sectionService = sectionService;
        this.sectionName = sectionName;
        this.viewPageView = viewPageView;
        this.editPageView = editPageView;
        this.viewPageAddress = viewPageAddress;
    }

    public String showViewPage(ModelMap model) {
        try {
            model.addAttribute("section", this.sectionService.findByName(this.sectionName));
        } catch (DataAccessException e) {
            LogFactory.getLog(this.getClass()).error(null, e);
        }
        return this.viewPageView;
    }

    public String showEditPage(ModelMap model) {
        model.addAttribute("wysiwygSectionEditCommand", this.sectionService.findByName(this.sectionName));
        return this.editPageView;
    }

    @Transactional
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
            BindingResult result, SessionStatus status, ModelMap model) {
        // No validation necessary.

        command.setName(this.sectionName);

        try {
            // Stores the data.
            this.sectionService.update(command);

            // Clears the command object from the session.
            status.setComplete();
        } catch (DataAccessException e) {
            LogFactory.getLog(this.getClass()).error(null, e);
            result.reject("edit.changes-not-saved-due-to-database-error");
        }
        return this.editPageView;
    }

    @Transactional
    public String saveChangesAndFinishEditing(
            @ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command, BindingResult result,
            SessionStatus status, ModelMap model) {
        // No validation necessary.

        saveChanges(command, result, status, model);
        if (result.hasErrors()) {
            return this.editPageView;
        } else {
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, this.viewPageAddress);
            return AbstractMetaController.REDIRECTION_PAGE;
        }
    }

    public String cancelChangesAndFinishEditing(ModelMap model) {
        model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, this.viewPageAddress);
        return AbstractMetaController.REDIRECTION_PAGE;
    }
}
