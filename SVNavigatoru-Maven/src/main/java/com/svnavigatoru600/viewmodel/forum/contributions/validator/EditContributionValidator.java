package com.svnavigatoru600.viewmodel.forum.contributions.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.forum.contributions.EditContribution;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditContributionValidator extends AbstractContributionValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditContribution.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditContribution command = (EditContribution) target;
        checkNewText(command.getContribution().getText(), errors);
    }
}
