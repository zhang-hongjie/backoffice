package fr.zhj2074.backoffice.api.users;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class UserRep {
    private final int id;
    private final String email;

    public UserRep(@NotNull User user) {
        id = user.getId();
        email = user.getEmail();
    }
}
