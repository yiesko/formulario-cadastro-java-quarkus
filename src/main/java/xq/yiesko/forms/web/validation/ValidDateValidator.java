package xq.yiesko.forms.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private static final DateTimeFormatter BRAZILIAN_FORMATTER = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);

    public static LocalDate parseOrNull(
            String value
    ) {
        if (value == null) return null;

        var normalized = value.trim();
        if (normalized.isEmpty()) return null;

        try {
            return LocalDate.parse(normalized, BRAZILIAN_FORMATTER);
        } catch (DateTimeParseException _) {
            try {
                return LocalDate.parse(normalized, ISO_FORMATTER);
            } catch (DateTimeParseException _) {
                return null;
            }
        }
    }

    public static String formatForInput(
            LocalDate date
    ) {
        if (date == null) return null;

        return ISO_FORMATTER.format(date);
    }

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null || value.trim().isEmpty()) return false;

        var trimmed = value.trim();

        var parsedDate = parseOrNull(trimmed);
        if (parsedDate == null) return false;

        var now = LocalDate.now();

        return !parsedDate.isAfter(now) && !parsedDate.isBefore(MIN_DATE);
    }
}