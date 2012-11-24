package com.svnavigatoru600.web;

import org.springframework.beans.factory.annotation.Autowired;
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


@Controller
public class BoardController extends WysiwygSectionController {

	/**
	 * Constructor.
	 */
	@Autowired
	public BoardController(WysiwygSectionDao sectionDao) {
		super(sectionDao);
		super.sectionName = WysiwygSectionName.BOARD;
		super.viewPageView = "viewBoard";
		super.editPageView = "editBoard";
		super.viewPageAddress = "/vybor/";
	}

	@RequestMapping(value = "/vybor/", method = RequestMethod.GET)
	public String showViewPage(ModelMap model) {
		return super.showViewPage(model);
	}

	@RequestMapping(value = "/vybor/editace/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String showEditPage(ModelMap model) {
		return super.showEditPage(model);
	}

	@RequestMapping(value = "/vybor/editace/ulozit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChanges(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChanges(command, result, status, model);
	}

	@RequestMapping(value = "/vybor/editace/ulozit-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String saveChangesAndFinishEditing(@ModelAttribute("wysiwygSectionEditCommand") WysiwygSection command,
			BindingResult result, SessionStatus status, ModelMap model) {
		return super.saveChangesAndFinishEditing(command, result, status, model);
	}

	@RequestMapping(value = "/vybor/editace/neukladat-a-skoncit/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String cancelChangesAndFinishEditing(ModelMap model) {
		return super.cancelChangesAndFinishEditing(model);
	}
}
