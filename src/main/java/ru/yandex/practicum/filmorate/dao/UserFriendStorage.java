package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class UserFriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.debug("/deleteFriend");
        log.debug("userId: {}, friendId: {}", userId, friendId);
        jdbcTemplate.update(DELETE_FRIEND.query, userId, friendId);
    }

    public void addFriend(int userId, int friendId) {
        log.debug("/addFriend");
        log.debug("userId: {}, friendId: {}");
        jdbcTemplate.update(ADD_FRIEND.query, userId, friendId);
    }
}