package xq.yiesko.forms.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import xq.yiesko.forms.domain.RegistrationForm;

import java.util.List;

@ApplicationScoped
public class RegistrationFormRepository implements PanacheRepository<RegistrationForm> {
    public List<RegistrationForm> findLatest(
            int limit
    ) {
        return findAll(Sort.descending("createdAt")).page(0, limit).list();
    }
}