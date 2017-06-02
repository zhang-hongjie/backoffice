package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.IntegrationTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GetUserCommandTest extends IntegrationTest {

    @Autowired
    private GetUserCommand getUserCommand;

    @Test
    public void get_existing_user() throws Exception {
        jdbc.execute("INSERT INTO users(id, email, login) VALUES (1, 'foo', 'login')");

        @NotNull Optional<User> user = getUserCommand.byId(1);

        assertThat(user.get().getEmail()).isEqualTo("foo");
    }

    @Test
    public void get_unknown_user() throws Exception {
        assertThat(getUserCommand.byId(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void get_existing_by_login() throws Exception {
        jdbc.execute("INSERT INTO users(id, email, login) VALUES (1, 'foo', 'login')");

        @NotNull Optional<User> user = getUserCommand.byLogin("login");

        assertThat(user.get().getLogin()).isEqualTo("login");
    }
}
