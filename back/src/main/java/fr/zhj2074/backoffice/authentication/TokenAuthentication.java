package fr.zhj2074.backoffice.authentication;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.zhj2074.backoffice.api.users.User;
import fr.zhj2074.backoffice.api.users.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class TokenAuthentication {

    private final Cache<String/* token */, String/* id_user */> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.DAYS) // Expire après une journée
            .build();


    public UserId validateToken(String token) {
        String userId = tokenCache.getIfPresent(token);
        if (nonNull(userId)) {
            return new UserId(userId);
        }
        log.debug("There no session for token " + token);
        throw new MissingTokenException();
    }

    public void addTokenToUser(User user, String token) {
        tokenCache.put(token, String.valueOf(user.getId()));
    }
}
