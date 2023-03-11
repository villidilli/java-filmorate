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
    FILM_GET_ALL("SELECT * FROM films ORDER BY id_film"),
    MPA_GET_ALL("SELECT * FROM mpa ORDER BY id_mpa"),
    GENRES_GET_ALL("SELECT * FROM genres ORDER BY id_genre"),
    COMMON_FRIENDS_USERS("SELECT id_user AS mutual_friendship FROM USER_FRIEND WHERE id_user IN " +
            "(SELECT id_friend FROM user_friend uf2 WHERE uf2.id_user=?) AND uf.id_friend = ?"),
    USER_GET_BY_ID("SELECT * FROM users WHERE id_user=?"),
    FILM_GET_BY_ID("SELECT * FROM films WHERE id_film=?"),
    USER_UPDATE("UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id_user=?"),
    ADD_FRIEND("INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)"),
    GET_FRIENDS_AS_USER("SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user= ?)"),
    GET_FRIENDS_AS_ID("SELECT id_friend FROM user_friend WHERE id_user=?"),
    DELETE_FRIEND("DELETE FROM user_friend WHERE id_user=? AND id_friend=?"),
    MPA_GET_BY_ID("SELECT * FROM mpa WHERE id_mpa = ?"),
    GENRE_GET_BY_ID("SELECT * FROM genres WHERE id_genre = ?"),
    GENRES_GET_BY_FILM_ID("SELECT * FROM genres WHERE id_genre IN " +
                            "(SELECT id_genre FROM film_genre WHERE id_film = ?) ORDER BY id_genre"),
    FILM_GENRE_SAVE("INSERT INTO film_genre (id_film, id_genre) VALUES (?,?)"),
    FILM_LIKE_SAVE("INSERT INTO film_like (id_film, id_user) VALUES (?,?)"),
    RATE_GET_BY_FILM_ID("SELECT COUNT(DISTINCT id_user) FROM film_like WHERE id_film = ?"),
    FILM_UPDATE_FILMS("UPDATE films SET name=?, description=?, release_date=?, duration=?, id_mpa=? WHERE id_film=?"),
    FILM_GENRE_DELETE_BY_FILM_ID("DELETE FROM FILM_GENRE WHERE ID_FILM = ?"),
    FILM_UPDATE_ID_MPA("UPDATE films SET id_mpa=? WHERE id_film=?");

    final String query;

    DbQuery(String query) {
        this.query = query;
    }
}