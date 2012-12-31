package com.svnavigatoru600.web.remostav;

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
import com.svnavigatoru600.repository.WysiwygSectionDao;
import com.svnavigatoru600.web.AbstractWysiwygSectionController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ContactController extends AbstractWysiwygSectionController {

    /**
     * Constructor.
     */
    @Inject
    public ContactController(WysiwygSectionDao sectionDao) {
        super(sectionDao);
        super.sectionName = WysiwygSectionName.REMOSTAV_CONTACT;
        super.viewPageView = "viewRemostavContact";
        super.editPageView = "editRemostavContact";
        super.viewPageAddress = "/remostav/kontakt/";
    }

    @Override
    @RequestMapping(value = "/remostav/kontakt/", method = RequestMethod.GET)
    public String showViewPage(ModelMap model) {
        return super.showViewPage(model);
    }

    @Override
    @RequestMapping(value = "/remostav/kontakt/editace/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String showEditPage(ModelMap model) {
        return super.showEditPage(model);
    }

    @Override
    @RequestMapping(value = "/remostav/kontakt/editace/ulozit/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
            BindingResult result, SessionStatus status, ModelMap model) {
        return super.saveChanges(command, result, status, model);
    }

    @Override
    @RequestMapping(value = "/remostav/kontakt/editace/ulozit-a-skoncit/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String saveChangesAndFinishEditing(
            @ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command, BindingResult result,
            SessionStatus status, ModelMap model) {
        return super.saveChangesAndFinishEditing(command, result, status, model);
    }

    @Override
    @RequestMapping(value = "/remostav/kontakt/editace/neukladat-a-skoncit/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String cancelChangesAndFinishEditing(ModelMap model) {
        return super.cancelChangesAndFinishEditing(model);
    }
}
