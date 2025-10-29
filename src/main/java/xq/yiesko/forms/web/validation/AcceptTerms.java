package xq.yiesko.forms.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptTermsValidator.class)
@Documented
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public @interface AcceptTerms {
    String message() default "Você deve aceitar os termos para enviar o formulário.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}