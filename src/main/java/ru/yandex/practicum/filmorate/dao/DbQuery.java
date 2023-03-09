package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;


public enum DbQuery {
    USERS_TABLE("users"),
    FILMS_TABLE("films"),
    USER_FRIEND_TABLE("user_friend"),
    USER_ID("id_user"),
    FILM_ID("id_film"),
    USER_ADD("INSERT INTO users (login, name, email, birthday VALUES (?,?,?,?)"),
    USER_GET_ALL("SELECT * FROM users"),
    FILM_GET_ALL("SELECT * FROM films"),
    USER_GET_BY_ID("SELECT * FROM users WHERE id_user=?"),
    FILM_GET_BY_ID("SELECT * FROM films WHERE id_film=?"),
    USER_UPDATE("UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id_user=?"),
    ADD_FRIEND("INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)"),
    GET_FRIENDS_AS_USER("SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user= ?)"),
    GET_FRIENDS_AS_ID("SELECT id_friend FROM user_friend WHERE id_user=?"),
    DELETE_FRIEND("DELETE FROM user_friend WHERE id_user=? AND id_friend=?"),
    MPA_GET_BY_ID("SELECT FROM mpa WHERE id_mpa=?"),
    MPA_GET_NAME_BY_ID("SELECT * FROM mpa WHERE id_mpa = ?"),
    FILM_UPDATE_ID_MPA("UPDATE films SET id_mpa=? WHERE id_film=?");

    final String query;

    DbQuery(String query) {
        this.query = query;
    }
}