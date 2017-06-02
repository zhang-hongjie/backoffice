package fr.zhj2074.backoffice.api.users;


import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateUserCommandTest extends IntegrationTest {

    @Autowired
    private CreateUserCommand create;
    @Autowired
    private ListUsersCommand users;

    @Test
    public void create() throws Exception {
        create.execute(new UserDataRep("foo@foo.com", "login"));

        assertThat(users.all().get(0).getEmail()).isEqualTo("foo@foo.com");
    }

    @Test
    public void return_generated_key() throws Exception {
        int execute = create.execute(new UserDataRep("foo@foo.com", "login"));

        assertThat(execute).isGreaterThan(0);
    }
}
