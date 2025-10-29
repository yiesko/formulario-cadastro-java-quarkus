package xq.yiesko.forms.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 254;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Predicate<String> EMAIL_PREDICATE = EMAIL_PATTERN.asMatchPredicate();

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null || value.isBlank()) return false;

        var s = value.trim();
        var len = s.length();

        if (len < MIN_LENGTH || len > MAX_LENGTH) return false;

        var at = s.indexOf('@');

        if (at <= 0) return false;
        if (s.indexOf('@', at + 1) != -1) return false;

        return EMAIL_PREDICATE.test(s);
    }
}