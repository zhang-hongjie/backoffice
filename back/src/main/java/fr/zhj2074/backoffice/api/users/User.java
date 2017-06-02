package fr.zhj2074.backoffice.api.users;

import lombok.Value;

@Value
public class User {
    private final int id;
    private final String email;
    private final String login;
}
