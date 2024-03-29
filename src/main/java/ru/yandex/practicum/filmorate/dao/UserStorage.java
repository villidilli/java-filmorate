package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

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
        log.debug("income user: {}", user.toString());
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
        log.debug("userId: {}", userId);
        return jdbcTemplate.query(USER_GET_BY_ID.query, new UserMapper(), userId).stream()
                .findAny()
                .orElse(null);
    }

    public List<User> getFriendsByUser(Integer userId) {
        log.debug("/getFriendsByUser");
        log.debug("income userid: {}", userId);
        return jdbcTemplate.query(GET_FRIENDS_AS_USER.query, new UserMapper(), userId);
    }

    public boolean isExist(Integer userId) {
        log.debug("/isExist");
        try {
            jdbcTemplate.queryForMap("SELECT id_user FROM users WHERE id_user = ?", userId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        log.debug("/getCommonFriends");
        log.debug("userId1: {}, userId2: {}", userId1, userId2);
        return jdbcTemplate.query(GET_COMMON_FRIENDS.query, new UserMapper(), userId1, userId2);
    }
}