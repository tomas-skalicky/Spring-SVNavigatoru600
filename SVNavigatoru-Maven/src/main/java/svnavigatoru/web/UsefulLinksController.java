package svnavigatoru.web;

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

@Controller
public class UsefulLinksController extends WysiwygSectionController {

	/**
	 * Constructor.
	 */
	@Autowired
	public UsefulLinksController(WysiwygSectionDao sectionDao) {
		super(sectionDao);
		super.sectionName = WysiwygSectionName.USEFUL_LINKS;
		super.viewPageView = "viewUsefulLinks";
		super.editPageView = "editUsefulLinks";
		super.viewPageAddress = "/uzitecne-odkazy/";
	}

	@RequestMapping(value = "/uzitecne-odkazy/", method = RequestMethod.GET)
	public String showViewPage(ModelMap model) {
		return super.showViewPage(model);
	}

	@RequestMapping(value = "/uzitecne-odkazy/editace/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String showEditPage(ModelMap model) {
		return super.showEditPage(model);
	}

	@RequestMapping(value = "/uzitecne-odkazy/editace/ulozit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChanges(command, result, status, model);
	}

	@RequestMapping(value = "/uzitecne-odkazy/editace/ulozit-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChangesAndFinishEditing(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChangesAndFinishEditing(command, result, status, model);
	}

	@RequestMapping(value = "/uzitecne-odkazy/editace/neukladat-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String cancelChangesAndFinishEditing(ModelMap model) {
		return super.cancelChangesAndFinishEditing(model);
	}
}
