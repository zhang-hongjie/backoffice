package fr.zhj2074.backoffice.api.users;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Pattern;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class UserDataRep {
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    @UniqueEmail
    private final String email;
    @UniqueEmail
    private final String login;
}
