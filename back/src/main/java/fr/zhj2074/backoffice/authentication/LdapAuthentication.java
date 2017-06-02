package fr.zhj2074.backoffice.authentication;


import com.google.common.annotations.VisibleForTesting;
import fr.zhj2074.backoffice.api.users.GetUserCommand;
import fr.zhj2074.backoffice.api.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.InvalidNameException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class LdapAuthentication {

    private final LdapTemplate ldapTemplate;
    private final String ldapBase;
    private final GetUserCommand getUserCommand;
    private final TokenAuthentication tokenAuthentication;

    @Autowired
    public LdapAuthentication(LdapTemplate ldapTemplate,
                              @Value("${ldap.base}") String ldapBase,
                              GetUserCommand getUserCommand,
                              TokenAuthentication tokenAuthentication) {

        this.ldapTemplate = ldapTemplate;
        this.ldapBase = ldapBase;
        this.getUserCommand = getUserCommand;
        this.tokenAuthentication = tokenAuthentication;
    }

    public String authenticate(String username, String password) {
        Filter filter = new EqualsFilter("sAMAccountName", username);
        boolean authenticated;
        try {
//            authenticated = ldapTemplate.authenticate(ldapBase, filter.encode(), decodeBase64(password));
            authenticated = true;
        } catch (InvalidNameException e) {
            log.error("An error occured when authenticating on LDAP", e);
            authenticated = false;
        }
        if (!authenticated) {
            log.error("The user {} is not authenticated in ldap", username);
            throw new UnknownUserException();
        }
        User user = filterChargeDossier(username).orElseThrow(() -> {
            log.error("The user {} is not a chargé de dossier", username);
            return new UnknownUserException();
        });

        String token = generateToken();
        tokenAuthentication.addTokenToUser(user, token);
        log.debug("The user is authenticated by login {} ", username);
        return token;
    }

    private Optional<User> filterChargeDossier(String username) {
        return getUserCommand.byLogin(username);
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @VisibleForTesting
    String decodeBase64(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new String(decoded);
    }
}
