package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class UserStorage implements RequestableStorage<User> {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USERS_TABLE.query)
                .usingGeneratedKeyColumns(USER_ID.query);
    }

    @Override
    public int addAndReturnId(User user) {
        log.debug("/addUserAndReturnId");
        return jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user)).intValue();
    }

    @Override
    public void update(User user) {
        log.debug("/updateUser");
        log.debug("income user: " + user.toString());
        jdbcTemplate.update(USER_UPDATE.query,
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
    }

    @Override
    public List<User> getAll() {
        log.debug("/getAll");
        return jdbcTemplate.query(USER_GET_ALL.query, new UserMapper());
    }

    @Override
    public User getById(Integer userId) {
        log.debug("/getById");
        return jdbcTemplate.query(USER_GET_BY_ID.query, new UserMapper(), userId).stream()
                .findAny()
                .orElse(null);
    }

    public List<User> getFriendsAsUser(Integer userId) { //для полного представления друзей
        log.debug("/getFriendsAsUser");
        log.debug("income userid: " + userId);
        return jdbcTemplate.query(GET_FRIENDS_AS_USER.query, new UserMapper(), userId);
    }
}