package xq.yiesko.forms.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

import java.net.URI;
import java.time.LocalDate;
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
    public TemplateInstance index(@QueryParam("success") String successMessage) {
        var request = new RegistrationFormRequest().withDefaults();
        return render(request, Map.of(), successMessage);
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

    @POST
    @Path("form/delete/{id}")
    @Transactional
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance delete(@PathParam("id") Long id) {
        var entity = service.findById(id);
        if (entity == null) {
            var cleanForm = new RegistrationFormRequest().withDefaults();
            return render(cleanForm, Map.of(), null);
        }
        
        var fullName = entity.getFullName();
        service.deleteById(id);
        
        var cleanForm = new RegistrationFormRequest().withDefaults();
        var successMessage = "Cadastro de %s excluído com sucesso.".formatted(fullName);
        return render(cleanForm, Map.of(), successMessage);
    }

    @GET
    @Path("form/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response edit(@PathParam("id") Long id) {
        var entity = service.findById(id);
        if (entity == null) return Response.seeOther(URI.create("/")).build();
        
        var request = RegistrationFormRequest.fromEntity(entity);
        return Response.ok(renderForEdit(request, Map.of(), entity.getId())).build();
    }

    @POST
    @Path("form/update/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance update(
            @PathParam("id") Long id,
            @BeanParam RegistrationFormRequest request
    ) {
        var errors = collectErrors(request);
        if (!errors.isEmpty()) return renderForEdit(request, errors, id);

        var updated = service.update(id, request.toCommand());
        if (updated == null) {
            var cleanForm = new RegistrationFormRequest().withDefaults();
            return render(cleanForm, Map.of(), null);
        }

        var cleanForm = new RegistrationFormRequest().withDefaults();
        var successMessage = "Cadastro atualizado com sucesso para %s.".formatted(updated.getFullName());
        return renderAfterUpdate(cleanForm, successMessage);
    }

    private TemplateInstance render(
            RegistrationFormRequest formData,
            Map<String, String> errors,
            String successMessage
    ) {
        var registrations = service.listLatest(5);
        return form.data("form", formData)
                .data("errors", errors)
                .data("successMessage", successMessage)
                .data("alertType", "success")
                .data("hasErrors", !errors.isEmpty())
                .data("registrations", registrations)
                .data("today", LocalDate.now())
                .data("editingId", null);
    }

    private TemplateInstance renderForEdit(
            RegistrationFormRequest formData,
            Map<String, String> errors,
            Long editingId
    ) {
        var registrations = service.listLatest(5);
        return form.data("form", formData)
                .data("errors", errors)
                .data("successMessage", null)
                .data("alertType", "info")
                .data("hasErrors", !errors.isEmpty())
                .data("registrations", registrations)
                .data("today", LocalDate.now())
                .data("editingId", editingId);
    }

    private TemplateInstance renderAfterUpdate(
            RegistrationFormRequest formData,
            String successMessage
    ) {
        var registrations = service.listLatest(5);
        return form.data("form", formData)
                .data("errors", Map.of())
                .data("successMessage", successMessage)
                .data("alertType", "info")
                .data("hasErrors", false)
                .data("registrations", registrations)
                .data("today", LocalDate.now())
                .data("editingId", null);
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