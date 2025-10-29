package xq.yiesko.forms.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import xq.yiesko.forms.service.RegistrationFormService;
import xq.yiesko.forms.web.validation.AcceptTerms;
import xq.yiesko.forms.web.validation.ValidDate;
import xq.yiesko.forms.web.validation.ValidEmail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Path("/")
public class FormPageResource {

    /* consistent way to ensure that the checking order is appropriate */
    private static final Map<Class<?>, Integer> CONSTRAINT_PRIORITY = Map.of(
            NotBlank.class, 0,
            AcceptTerms.class, 0,
            ValidDate.class, 1,
            ValidEmail.class, 1,
            Pattern.class, 2
    );

    private static final int DEFAULT_PRIORITY = 10;

    @Inject
    Template form;

    @Inject
    RegistrationFormService service;

    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        var request = new RegistrationFormRequest().withDefaults();
        return render(request, Map.of(), null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance submit(
            @BeanParam RegistrationFormRequest request
    ) {
        var errors = collectErrors(request);

        if (!errors.isEmpty()) {
            return render(request, errors, null);
        }

        var saved = service.register(request.toCommand());
        var cleanForm = new RegistrationFormRequest().withDefaults();

        return render(cleanForm, Map.of(), "Cadastro realizado com sucesso para %s.".formatted(saved.getFullName()));
    }

    @GET
    @Path("api/forms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        var summaries = service.listAll();
        return Response.ok(summaries).build();
    }

    private TemplateInstance render(
            RegistrationFormRequest formData,
            Map<String, String> errors,
            String successMessage
    ) {
        var registrations = service.listLatest(10);
        return form.data("form", formData)
                .data("errors", errors)
                .data("successMessage", successMessage)
                .data("hasErrors", !errors.isEmpty())
                .data("registrations", registrations)
                .data("today", java.time.LocalDate.now().toString());
    }

    private Map<String, String> collectErrors(
            RegistrationFormRequest request
    ) {
        var violations = validator.validate(request);
        var errors = new LinkedHashMap<String, String>(violations.size());
        var priorities = new HashMap<String, Integer>(violations.size());

        for (var violation : violations) {
            var field = violation.getPropertyPath().toString();
            var annotationType = violation.getConstraintDescriptor().getAnnotation().annotationType();
            var priority = CONSTRAINT_PRIORITY.getOrDefault(annotationType, DEFAULT_PRIORITY);

            var currentPriority = priorities.get(field);

            if (currentPriority == null || priority < currentPriority) {
                priorities.put(field, priority);
                errors.put(field, violation.getMessage());
            }
        }

        return errors;
    }
}