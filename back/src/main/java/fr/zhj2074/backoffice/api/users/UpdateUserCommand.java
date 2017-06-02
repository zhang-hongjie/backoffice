package fr.zhj2074.backoffice.api.users;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UpdateUserCommand {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public void execute(int id, @NotNull UserDataRep data) {
        String sql = "update users set email=:email where id=:id";
        int affectedRows = jdbc.update(sql, new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("email", data.getEmail())
        );

        if (affectedRows != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, affectedRows);
        }
    }
}
