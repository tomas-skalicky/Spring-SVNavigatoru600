package com.svnavigatoru600.service.forum.threads.validator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.service.forum.contributions.validator.NewContributionValidator;
import com.svnavigatoru600.viewmodel.forum.threads.NewThread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewThreadValidator extends AbstractThreadValidator {

    private NewContributionValidator validator;

    @Inject
    public NewThreadValidator(NewContributionValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return NewThread.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewThread command = (NewThread) target;
        this.checkNewName(command.getThread().getName(), errors);
        this.validator.checkNewText(command.getContribution().getText(), errors);
    }
}
