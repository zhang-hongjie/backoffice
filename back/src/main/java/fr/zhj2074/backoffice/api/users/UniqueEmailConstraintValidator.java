package fr.zhj2074.backoffice.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueEmailConstraintValidator implements ConstraintValidator<UniqueEmail, String> {

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public UniqueEmailConstraintValidator(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void initialize(UniqueEmail unique) {
    }

    @Override
    @Transactional
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !jdbc.queryForObject("select exists(select 1 from users where email LIKE :email)",
            new MapSqlParameterSource("email", value), Boolean.class);
    }
}
