package svnavigatoru.web.remostav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import svnavigatoru.domain.WysiwygSection;
import svnavigatoru.domain.WysiwygSectionName;
import svnavigatoru.repository.WysiwygSectionDao;
import svnavigatoru.web.WysiwygSectionController;

@Controller
public class ContactController extends WysiwygSectionController {

	/**
	 * Constructor.
	 */
	@Autowired
	public ContactController(WysiwygSectionDao sectionDao) {
		super(sectionDao);
		super.sectionName = WysiwygSectionName.REMOSTAV_CONTACT;
		super.viewPageView = "viewRemostavContact";
		super.editPageView = "editRemostavContact";
		super.viewPageAddress = "/remostav/kontakt/";
	}

	@RequestMapping(value = "/remostav/kontakt/", method = RequestMethod.GET)
	public String showViewPage(ModelMap model) {
		return super.showViewPage(model);
	}

	@RequestMapping(value = "/remostav/kontakt/editace/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String showEditPage(ModelMap model) {
		return super.showEditPage(model);
	}

	@RequestMapping(value = "/remostav/kontakt/editace/ulozit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChanges(command, result, status, model);
	}

	@RequestMapping(value = "/remostav/kontakt/editace/ulozit-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChangesAndFinishEditing(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChangesAndFinishEditing(command, result, status, model);
	}

	@RequestMapping(value = "/remostav/kontakt/editace/neukladat-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String cancelChangesAndFinishEditing(ModelMap model) {
		return super.cancelChangesAndFinishEditing(model);
	}
}
