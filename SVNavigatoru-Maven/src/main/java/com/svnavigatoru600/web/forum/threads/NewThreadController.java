package com.svnavigatoru600.web.forum.threads;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.threads.validator.NewThreadValidator;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.forum.threads.NewThread;
import com.svnavigatoru600.web.Configuration;

@Controller
public class NewThreadController extends NewEditThreadController {

    private static final String REQUEST_MAPPING_BASE_URL = NewThreadController.BASE_URL + "novy/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.threads.adding-failed-due-to-database-error";

    /**
     * Constructor.
     */
    @Autowired
    public NewThreadController(ThreadDao threadDao, NewThreadValidator validator, MessageSource messageSource) {
        super(threadDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {

        NewThread command = new NewThread();

        Thread thread = new Thread();
        command.setThread(thread);
        Contribution contribution = new Contribution();
        command.setContribution(contribution);

        model.addAttribute(NewThreadController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new thread is stored to the repository. Otherwise, returns back to
     * the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = NewThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewThreadController.COMMAND) NewThread command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new thread and its first contribution.
        Thread newThread = command.getThread();
        User author = UserUtils.getLoggedUser();
        newThread.setAuthor(author);

        Contribution firstContribution = command.getContribution();
        firstContribution.setThread(newThread);
        firstContribution.setAuthor(author);

        List<Contribution> contributions = new ArrayList<Contribution>();
        contributions.add(command.getContribution());
        newThread.setContributions(contributions);
        // End of update.

        try {
            // Stores both the thread and the contribution to the repository.
            this.threadDao.save(newThread);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%svytvoreno/", NewThreadController.BASE_URL));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newThread, e);
            result.reject(NewThreadController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.NEW.getViewName();
    }
}
