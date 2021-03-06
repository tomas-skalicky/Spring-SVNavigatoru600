package com.svnavigatoru600.web;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionNameEnum;
import com.svnavigatoru600.service.WysiwygSectionService;
import com.svnavigatoru600.web.url.RemostavContactsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RemostavContactController extends AbstractWysiwygSectionController {

    @Inject
    public RemostavContactController(final WysiwygSectionService sectionService) {
        super(sectionService, WysiwygSectionNameEnum.REMOSTAV_CONTACT, "viewRemostavContact", "editRemostavContact",
                RemostavContactsUrlParts.BASE_URL);
    }

    @Override
    @GetMapping(value = RemostavContactsUrlParts.BASE_URL)
    public String showViewPage(final ModelMap model) {
        return super.showViewPage(model);
    }

    @Override
    @GetMapping(value = RemostavContactsUrlParts.EDIT_URL)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String showEditPage(final ModelMap model) {
        return super.showEditPage(model);
    }

    @Override
    @PostMapping(value = RemostavContactsUrlParts.SAVE_EDIT_URL)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") final WysiwygSection command,
            final BindingResult result, final SessionStatus status, final ModelMap model) {
        return super.saveChanges(command, result, status, model);
    }

    @Override
    @PostMapping(value = RemostavContactsUrlParts.SAVE_EDIT_AND_EXIT_URL)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChangesAndFinishEditing(@ModelAttribute("wysiwygSectionEditCommand") final WysiwygSection command,
            final BindingResult result, final SessionStatus status, final ModelMap model) {
        return super.saveChangesAndFinishEditing(command, result, status, model);
    }

    @Override
    @PostMapping(value = RemostavContactsUrlParts.DONT_SAVE_EDIT_AND_EXIT_URL)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String cancelChangesAndFinishEditing(final ModelMap model) {
        return super.cancelChangesAndFinishEditing(model);
    }
}
