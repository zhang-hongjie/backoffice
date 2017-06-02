package fr.zhj2074.backoffice.api.users;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login")
        );
    }
}
