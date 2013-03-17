package com.svnavigatoru600.viewmodel.forum.contributions.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.forum.contributions.NewContribution;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewContributionValidator extends AbstractContributionValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewContribution.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewContribution command = (NewContribution) target;
        checkNewText(command.getContribution().getText(), errors);
    }
}
