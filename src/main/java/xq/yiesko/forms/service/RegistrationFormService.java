package xq.yiesko.forms.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import xq.yiesko.forms.domain.RegistrationForm;
import xq.yiesko.forms.repository.RegistrationFormRepository;
import xq.yiesko.forms.service.dto.RegistrationFormCommand;
import xq.yiesko.forms.service.dto.RegistrationFormSummary;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class RegistrationFormService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                    .withZone(ZoneId.systemDefault());
    @Inject
    RegistrationFormRepository repository;

    @Transactional
    public RegistrationForm register(
            RegistrationFormCommand command
    ) {
        var entity = new RegistrationForm();
        entity.setFullName(command.fullName());
        entity.setAddress(command.address());
        entity.setStreet(command.street());
        entity.setHouseNumber(command.houseNumber());
        entity.setBirthDate(command.birthDate());
        entity.setEmail(command.email());
        entity.setCity(command.city());
        entity.setState(command.state());
        entity.setAcceptedTerms(command.acceptedTerms());
        repository.persist(entity);
        return entity;
    }

    public List<RegistrationFormSummary> listAll() {
        return repository.findAll(Sort.descending("createdAt")).stream()
                .map(this::toSummary)
                .toList();
    }

    public List<RegistrationFormSummary> listLatest(
            int limit
    ) {
        return repository.findLatest(limit).stream()
                .map(this::toSummary)
                .toList();
    }

    private RegistrationFormSummary toSummary(
            RegistrationForm entity
    ) {
        var createdAtFormatted = entity.getCreatedAt() == null
                ? "-"
                : DATE_TIME_FORMATTER.format(entity.getCreatedAt());

        return new RegistrationFormSummary(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getCity(),
                entity.getState(),
                entity.getBirthDate(),
                entity.getBirthDate() == null ? "-" : DATE_FORMATTER.format(entity.getBirthDate()),
                entity.getCreatedAt(),
                createdAtFormatted
        );
    }
}