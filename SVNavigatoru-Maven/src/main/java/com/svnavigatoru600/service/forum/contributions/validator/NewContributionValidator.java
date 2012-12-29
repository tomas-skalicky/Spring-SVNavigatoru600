package com.svnavigatoru600.service.forum.contributions.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.forum.contributions.NewContribution;

@Service
public class NewContributionValidator extends ContributionValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewContribution.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewContribution command = (NewContribution) target;
        this.checkNewText(command.getContribution().getText(), errors);
    }
}
