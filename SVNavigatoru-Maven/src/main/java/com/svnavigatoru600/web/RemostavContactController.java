package com.svnavigatoru600.web;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.service.WysiwygSectionService;
import com.svnavigatoru600.url.RemostavContactsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RemostavContactController extends AbstractWysiwygSectionController {

    /**
     * Constructor.
     */
    @Inject
    public RemostavContactController(WysiwygSectionService sectionService) {
        super(sectionService, WysiwygSectionName.REMOSTAV_CONTACT, "viewRemostavContact",
                "editRemostavContact", RemostavContactsUrlParts.BASE_URL);
    }

    @Override
    @RequestMapping(value = RemostavContactsUrlParts.BASE_URL, method = RequestMethod.GET)
    public String showViewPage(ModelMap model) {
        return super.showViewPage(model);
    }

    @Override
    @RequestMapping(value = RemostavContactsUrlParts.EDIT_URL, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String showEditPage(ModelMap model) {
        return super.showEditPage(model);
    }

    @Override
    @RequestMapping(value = RemostavContactsUrlParts.SAVE_EDIT_URL, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
            BindingResult result, SessionStatus status, ModelMap model) {
        return super.saveChanges(command, result, status, model);
    }

    @Override
    @RequestMapping(value = RemostavContactsUrlParts.SAVE_EDIT_AND_EXIT_URL, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChangesAndFinishEditing(
            @ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command, BindingResult result,
            SessionStatus status, ModelMap model) {
        return super.saveChangesAndFinishEditing(command, result, status, model);
    }

    @Override
    @RequestMapping(value = RemostavContactsUrlParts.DONT_SAVE_EDIT_AND_EXIT_URL, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String cancelChangesAndFinishEditing(ModelMap model) {
        return super.cancelChangesAndFinishEditing(model);
    }
}
