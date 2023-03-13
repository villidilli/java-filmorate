package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.util.FriendMapper;
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

    public void addFriend(int userId, int friendId) {
        log.debug("/addFriend");
        log.debug("income userId / friendId [" + userId + "/" + friendId + "]");
        jdbcTemplate.update(ADD_FRIEND.query, userId, friendId);
    }

    public List<User> getFriendsAsUser(Integer userId) {
        log.debug("/getFriendsAsUser");
        log.debug("income userid: " + userId);
        return jdbcTemplate.query(GET_FRIENDS_AS_USER.query, new UserMapper(), userId);
    }

    public List<Friend> getFriendsAsFriend(Integer userId) { // для короткого представления списка друзей
        log.debug("/getFriendsAsFriend");
        return jdbcTemplate.query(GET_FRIENDS_AS_ID.query, new FriendMapper(), userId);
    }

    public List<Integer> getMutualFriendsId(Integer userId) {
        return jdbcTemplate.queryForList(COMMON_FRIENDS_USERS.query, Integer.class, userId, userId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        log.debug("/deleteFriend");
        jdbcTemplate.update(DELETE_FRIEND.query, id, friendId);
    }
}