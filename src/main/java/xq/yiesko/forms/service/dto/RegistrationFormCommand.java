package xq.yiesko.forms.service.dto;

import java.time.LocalDate;

public record RegistrationFormCommand(
        String fullName,
        String address,
        String street,
        String houseNumber,
        LocalDate birthDate,
        String email,
        String city,
        String state,
        boolean acceptedTerms
) {}