package fr.zhj2074.backoffice.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ListUsersCommand {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public List<User> all() {
        return jdbc.query("select * from users order by id", new UserRowMapper());
    }
}
