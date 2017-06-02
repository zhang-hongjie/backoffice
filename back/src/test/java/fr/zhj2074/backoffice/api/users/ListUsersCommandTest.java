package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListUsersCommandTest extends IntegrationTest {

    @Autowired
    private ListUsersCommand listUsers;

    @Test
    public void list_users() throws Exception {
        jdbc.execute("INSERT INTO users(id) VALUES (1)");
        jdbc.execute("INSERT INTO users(id) VALUES (2)");

        List<User> users = listUsers.all();

        assertThat(users.size()).isEqualTo(2);
    }
}
