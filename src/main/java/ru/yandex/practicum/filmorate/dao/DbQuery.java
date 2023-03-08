package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum DbQuery {
    USERS_TABLE("users"),
    USER_FRIEND_TABLE("user_friend"),
    USER_ID("id_user"),
    USER_ADD("INSERT INTO users (login, name, email, birthday VALUES (?,?,?,?)"),
    USER_GET_ALL("SELECT * FROM users"),
    FILM_GET_ALL("SELECT * FROM films"),
    USER_GET_BY_ID("SELECT * FROM users WHERE id_user=?"),
    USER_UPDATE("UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id_user=?"),
    ADD_FRIEND("INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)"),
    GET_FRIENDS("SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user= ?)"),
    DELETE_FRIEND("DELETE FROM user_friend WHERE id_user = ? AND id_friend =?");

    final String query;

    DbQuery(String query) {
        this.query = query;
    }
}