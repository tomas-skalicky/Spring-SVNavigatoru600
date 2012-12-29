package com.svnavigatoru600.web.forum.contributions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.contributions.NewContribution;
import com.svnavigatoru600.service.forum.contributions.validator.NewContributionValidator;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.web.Configuration;

@Controller
public class NewContributionController extends NewEditContributionController {

    private static final String REQUEST_MAPPING_BASE_URL = NewContributionController.BASE_URL
            + "{threadId}/prispevky/novy/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.contributions.adding-failed-due-to-database-error";
    private ThreadDao threadDao;

    @Autowired
    public void setThreadDao(ThreadDao threadDao) {
        this.threadDao = threadDao;
    }

    /**
     * Constructor.
     */
    @Autowired
    public NewContributionController(ContributionDao contributionDao, NewContributionValidator validator,
            MessageSource messageSource) {
        super(contributionDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        NewContribution command = new NewContribution();

        Contribution contribution = new Contribution();
        command.setContribution(contribution);
        command.setThreadId(threadId);

        model.addAttribute(NewContributionController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new contribution is stored to the repository. Otherwise, returns back
     * to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = NewContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(NewContributionController.COMMAND) NewContribution command, BindingResult result,
            SessionStatus status, @PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new contribution.
        Contribution newContribution = command.getContribution();
        newContribution.setAuthor(UserUtils.getLoggedUser());

        try {
            newContribution.setThread(this.threadDao.findById(threadId));

            // Saves the contribution to the repository.
            this.contributionDao.save(newContribution);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/prispevky/vytvoreno/", NewContributionController.BASE_URL, threadId));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newContribution, e);
            result.reject(NewContributionController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.NEW.getViewName();
    }
}
