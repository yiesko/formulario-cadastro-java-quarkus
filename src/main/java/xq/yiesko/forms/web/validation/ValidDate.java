package xq.yiesko.forms.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
@Documented
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public @interface ValidDate {
    String message() default "Use o formato AAAA-MM-DD (ex: 1990-05-15) e escolha uma data válida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}