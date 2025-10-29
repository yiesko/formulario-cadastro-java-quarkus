package xq.yiesko.forms.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "registration_forms")
@SuppressWarnings("unused")
public class RegistrationForm extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 180)
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "street", length = 150)
    private String street;

    @Column(name = "house_number", length = 32)
    private String houseNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email", length = 180)
    private String email;

    @Column(name = "city", length = 120)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "accepted_terms", nullable = false)
    private boolean acceptedTerms;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}