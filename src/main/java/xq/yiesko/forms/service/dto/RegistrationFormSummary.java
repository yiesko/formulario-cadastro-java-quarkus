package xq.yiesko.forms.service.dto;

import java.time.Instant;
import java.time.LocalDate;

public record RegistrationFormSummary(
        Long id,
        String fullName,
        String email,
        String city,
        String state,
        LocalDate birthDate,
        String birthDateFormatted,
        Instant createdAt,
        String createdAtFormatted
) {}