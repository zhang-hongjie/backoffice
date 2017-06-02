package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DeleteUserCommandTest extends IntegrationTest {

    @Autowired
    private DeleteUserCommand delete;
    @Autowired
    private CreateUserCommand create;
    @Autowired
    private ListUsersCommand users;

    @Test
    public void exesting_user() throws Exception {
        int id = create.execute(new UserDataRep("foo@foo.com", "login"));

        delete.execute(id);

        assertThat(users.all()).extracting("email").doesNotContain("foo@foo.com");
    }

    @Test
    public void unknown_user() throws Exception {
        assertThatThrownBy(() -> delete.execute(1))
            .isInstanceOf(JdbcUpdateAffectedIncorrectNumberOfRowsException.class);
    }
}
