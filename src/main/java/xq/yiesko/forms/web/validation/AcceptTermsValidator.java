package xq.yiesko.forms.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AcceptTermsValidator implements ConstraintValidator<AcceptTerms, Boolean> {
    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return Boolean.TRUE.equals(value);
    }
}