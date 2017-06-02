package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UniqueEmailConstraintValidatorTest extends IntegrationTest {

    @Autowired
    private CreateUserCommand createUserCommand;

    @Test
    public void valid_given_email_does_not_already_exist() throws Exception {
        UniqueEmailConstraintValidator validator = new UniqueEmailConstraintValidator(namedJdbc);
        createUserCommand.execute(new UserDataRep("foo@foo.com", "login"));

        assertThat(validator.isValid("bar@bar.com", null)).isTrue();
    }

    @Test
    public void invalid_given_email_exists() throws Exception {
        UniqueEmailConstraintValidator validator = new UniqueEmailConstraintValidator(namedJdbc);
        createUserCommand.execute(new UserDataRep("foo@foo.com", "login"));

        assertThat(!validator.isValid("foo@foo.com", null)).isTrue();
    }
}
