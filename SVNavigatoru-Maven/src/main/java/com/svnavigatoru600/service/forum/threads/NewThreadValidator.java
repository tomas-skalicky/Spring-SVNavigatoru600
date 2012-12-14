package com.svnavigatoru600.service.forum.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.service.forum.contributions.NewContributionValidator;

@Service
public class NewThreadValidator extends ThreadValidator {

    private NewContributionValidator validator;

    @Autowired
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
