package xq.yiesko.forms.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null || value.trim().isEmpty()) return false;

        var trimmed = value.trim();

        if (trimmed.length() != 10) return false;

        try {
            var parsedDate = LocalDate.parse(trimmed, FORMATTER);
            var now = LocalDate.now();

            return !parsedDate.isAfter(now) && !parsedDate.isBefore(MIN_DATE);

        } catch (DateTimeParseException e) {
            return false;
        }
    }
}