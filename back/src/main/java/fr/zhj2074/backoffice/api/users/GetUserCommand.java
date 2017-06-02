package fr.zhj2074.backoffice.api.users;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetUserCommand {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public @NotNull Optional<User> byId(int id) {
        try {
            return Optional.of(jdbc.queryForObject("SELECT * FROM users WHERE id = :id",
                new MapSqlParameterSource("id", id),
                new UserRowMapper()));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    public @NotNull Optional<User> byLogin(String login) {
        try {
            return Optional.of(jdbc.queryForObject("SELECT * FROM users WHERE login = :login",
                    new MapSqlParameterSource("login", login),
                    new UserRowMapper()));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }
}
