package fr.zhj2074.backoffice.authentication;


import fr.zhj2074.backoffice.api.users.GetUserCommand;
import fr.zhj2074.backoffice.api.users.User;
import org.junit.Test;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class LdapAuthenticationTest {

    private LdapTemplate ldapTemplate = mock(LdapTemplate.class);
    private String ldapBase = "CN=Nizar Chaouachi,OU=Utilisateurs Service DOSI,OU=Comptes Utilisateurs zhj2074,OU=zhj2074,DC=rse,DC=zhj2074,DC=int";
    private GetUserCommand getUserCommand = mock(GetUserCommand.class);
    private TokenAuthentication tokenAuthentication = mock(TokenAuthentication.class);
    private LdapAuthentication ldapAuthentication = new LdapAuthentication(ldapTemplate, ldapBase, getUserCommand, tokenAuthentication);

    @Test
    public void authenticate() throws Exception {
        String username = "username";
        String password = "password";
        String ecodedPassword = Base64.getEncoder().encodeToString(password.toString().getBytes());
        User user = new User(1, "email@email.com", username);
        when(ldapTemplate.authenticate(anyString(), eq(new EqualsFilter("sAMAccountName", username).encode()), eq(password))).thenReturn(true);
        when(getUserCommand.byLogin(username)).thenReturn(Optional.of(user));
        doNothing().when(tokenAuthentication).addTokenToUser(eq(user), anyString());

        String authentication = ldapAuthentication.authenticate(username, ecodedPassword);

        assertThat(authentication).isNotNull();
        assertThat(authentication).isNotEmpty();
        verify(tokenAuthentication).addTokenToUser(eq(user), anyString());
    }

    @Test
    public void ldap_authentication_failed() throws Exception {
        String username = "wrong_username";
        String password = "password";
        String ecodedPassword = Base64.getEncoder().encodeToString(password.toString().getBytes());
        when(ldapTemplate.authenticate(anyString(), eq(new EqualsFilter("sAMAccountName", username).encode()), eq(password))).thenReturn(false);

        assertThatThrownBy(() -> ldapAuthentication.authenticate(username, ecodedPassword))
            .isInstanceOf(UnknownUserException.class);
    }

    @Test
    public void is_not_charge_dossier_authentication_failed() throws Exception {
        String username = "username";
        String password = "password";
        String ecodedPassword = Base64.getEncoder().encodeToString(password.toString().getBytes());
        when(ldapTemplate.authenticate(anyString(), eq(new EqualsFilter("sAMAccountName", username).encode()), eq(password))).thenReturn(true);
        when(getUserCommand.byLogin(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ldapAuthentication.authenticate(username, ecodedPassword))
            .isInstanceOf(UnknownUserException.class);
    }

    @Test
    public void decode_base64() throws Exception {
        String toConvert = "an ecoded string";
        String converted = Base64.getEncoder().encodeToString(toConvert.toString().getBytes());

        String decoded = ldapAuthentication.decodeBase64(converted);

        assertThat(decoded).isEqualTo(toConvert);
    }
}
