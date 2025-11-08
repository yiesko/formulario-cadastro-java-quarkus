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
    String message() default "Use o formato DD-MM-AAAA (ex: 15-05-1990) e escolha uma data válida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}