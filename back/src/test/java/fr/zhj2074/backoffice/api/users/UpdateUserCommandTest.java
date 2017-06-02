package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UpdateUserCommandTest extends IntegrationTest {

    @Autowired
    private UpdateUserCommand update;
    @Autowired
    private CreateUserCommand create;
    @Autowired
    private ListUsersCommand users;

    @Test
    public void existing_user() throws Exception {
        int id = create.execute(new UserDataRep("foo@foo.com", "login"));

        update.execute(id, new UserDataRep("bar@foo.com", "login"));

        assertThat(users.all().get(0).getEmail()).isEqualTo("bar@foo.com");
        assertThat(users.all().get(0).getEmail()).isNotEqualTo("foo@foo.com");
    }

    @Test
    public void unknown_user() throws Exception {
        assertThatThrownBy(() -> update.execute(1, new UserDataRep("bar@foo.com", "login")))
            .isInstanceOf(JdbcUpdateAffectedIncorrectNumberOfRowsException.class);

    }
}
