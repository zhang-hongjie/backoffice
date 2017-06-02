package fr.zhj2074.backoffice.authentication;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TokenResult {
    private final String token;
}
