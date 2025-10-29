package xq.yiesko.forms.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmailValidator.class)
@Documented
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public @interface ValidEmail {
    String message() default "Informe um e-mail válido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}