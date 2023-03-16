package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
//import ru.yandex.practicum.filmorate.model.Friend;
//import ru.yandex.practicum.filmorate.util.FriendMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class UserFriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Friend> getFriendsAsFriend(Integer userId) { // для короткого представления друзей (только айди)
//        log.debug("/getFriendsAsFriend");
//        return jdbcTemplate.query(GET_FRIENDS_AS_ID.query, new FriendMapper(), userId);
//    }

    public List<Integer> getMutualFriendsId(Integer userId) {
        return jdbcTemplate.queryForList(COMMON_FRIENDS_USERS.query, Integer.class, userId, userId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        log.debug("/deleteFriend");
        jdbcTemplate.update(DELETE_FRIEND.query, id, friendId);
    }

    public void addFriend(int userId, int friendId) {
        log.debug("/addFriend");
        log.debug("income userId / friendId [" + userId + "/" + friendId + "]");
        jdbcTemplate.update(ADD_FRIEND.query, userId, friendId);
    }
}
