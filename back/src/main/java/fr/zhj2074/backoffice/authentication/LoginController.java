package fr.zhj2074.backoffice.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class LoginController {

    private final LdapAuthentication ldapAuthentication;

    @Autowired
    public LoginController(LdapAuthentication ldapAuthentication) {
        this.ldapAuthentication = ldapAuthentication;
    }

    @RequestMapping(method = GET, path = "/login")
    public TokenResult authenticateUser(@RequestParam String username, @RequestParam String password) {
        String token = ldapAuthentication.authenticate(username, password);
        return new TokenResult(token);
    }
}
