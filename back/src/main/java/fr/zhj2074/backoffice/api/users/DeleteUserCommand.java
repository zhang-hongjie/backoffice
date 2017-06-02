package fr.zhj2074.backoffice.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteUserCommand {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public void execute(int id) {
        String sql = "DELETE FROM users WHERE id=:id";
        int affectedRows = jdbc.update(sql, new MapSqlParameterSource("id", id));

        if (affectedRows != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, affectedRows);
        }
    }

    public void execute(String login) {
        String sql = "DELETE FROM users WHERE login=:login";
        int affectedRows = jdbc.update(sql, new MapSqlParameterSource("login", login));

        if (affectedRows != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, affectedRows);
        }
    }
}
