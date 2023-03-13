package ru.yandex.practicum.filmorate.util;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.exception.MapperException;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum)  {
        User user = new User();
        try {
            user.setId(rs.getInt("id_user"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        } catch (SQLException e) {
            throw new MapperException("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }
}