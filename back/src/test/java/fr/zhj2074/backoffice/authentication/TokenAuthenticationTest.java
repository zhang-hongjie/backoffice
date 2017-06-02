package fr.zhj2074.backoffice.authentication;


import fr.zhj2074.backoffice.api.users.User;
import fr.zhj2074.backoffice.api.users.UserId;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenAuthenticationTest {

    private TokenAuthentication tokenAuthentication = new TokenAuthentication();

    @Test
    public void valid_token() throws Exception {
        User user = new User(1, "email@email.com", "login");
        String token = "a token";
        tokenAuthentication.addTokenToUser(user, token);

        UserId theUser = tokenAuthentication.validateToken(token);

        assertThat(theUser.getUserId()).isEqualTo(String.valueOf(user.getId()));
    }
}
