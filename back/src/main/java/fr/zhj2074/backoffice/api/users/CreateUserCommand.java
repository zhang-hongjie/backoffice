package fr.zhj2074.backoffice.api.users;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CreateUserCommand {

    @Autowired
    private JdbcTemplate jdbc;

    public int execute(@NotNull UserDataRep rep) {
        Number id = new SimpleJdbcInsert(jdbc)
            .withTableName("users")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(new BeanPropertySqlParameterSource(rep));

        return id.intValue();
    }
}
