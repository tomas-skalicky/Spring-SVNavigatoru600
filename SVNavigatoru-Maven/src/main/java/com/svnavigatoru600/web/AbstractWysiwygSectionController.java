package com.svnavigatoru600.web;

import javax.inject.Inject;

import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
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
    public AbstractWysiwygSectionController(final WysiwygSectionService sectionService, final WysiwygSectionName sectionName,
            final String viewPageView, final String editPageView, final String viewPageAddress) {
        LogFactory.getLog(this.getClass()).debug("The WysiwygSectionController object created.");
        this.sectionService = sectionService;
        this.sectionName = sectionName;
        this.viewPageView = viewPageView;
        this.editPageView = editPageView;
        this.viewPageAddress = viewPageAddress;
    }

    public String showViewPage(final ModelMap model) {
        try {
            model.addAttribute("section", sectionService.findByName(sectionName));
        } catch (final DataAccessException e) {
            LogFactory.getLog(this.getClass()).error(null, e);
        }
        return viewPageView;
    }

    public String showEditPage(final ModelMap model) {
        model.addAttribute("wysiwygSectionEditCommand", sectionService.findByName(sectionName));
        return editPageView;
    }

    @Transactional
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") final WysiwygSection command, final BindingResult result,
            final SessionStatus status, final ModelMap model) {
        // No validation necessary.

        command.setName(sectionName);

        try {
            // Stores the data.
            sectionService.update(command);

            // Clears the command object from the session.
            status.setComplete();
        } catch (final DataAccessException e) {
            LogFactory.getLog(this.getClass()).error(null, e);
            result.reject("edit.changes-not-saved-due-to-database-error");
        }
        return editPageView;
    }

    @Transactional
    public String saveChangesAndFinishEditing(@ModelAttribute("wysiwygSectionEditCommand") final WysiwygSection command,
            final BindingResult result, final SessionStatus status, final ModelMap model) {
        // No validation necessary.

        saveChanges(command, result, status, model);
        if (result.hasErrors()) {
            return editPageView;
        } else {
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, viewPageAddress);
            return AbstractMetaController.REDIRECTION_PAGE;
        }
    }

    public String cancelChangesAndFinishEditing(final ModelMap model) {
        model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, viewPageAddress);
        return AbstractMetaController.REDIRECTION_PAGE;
    }
}
