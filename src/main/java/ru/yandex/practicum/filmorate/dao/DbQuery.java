package ru.yandex.practicum.filmorate.dao;

public enum DbQuery {
    USERS_TABLE("users"),
    FILMS_TABLE("films"),
    MPA_TABLE("mpa"),
    USER_ID("id_user"),
    FILM_ID("id_film"),
    MPA_ID("id_mpa"),
    USER_GET_ALL("SELECT * FROM users"),
    FILM_GET_ALL("SELECT * FROM films ORDER BY id_film"),
    MPA_GET_ALL("SELECT id_mpa, name AS mpa_name FROM mpa ORDER BY id_mpa"),
    GENRES_GET_ALL("SELECT * FROM genres ORDER BY id_genre"),
    COMMON_FRIENDS_USERS("SELECT id_user AS mutual_friendship FROM USER_FRIEND WHERE id_user IN " +
            "(SELECT id_friend FROM user_friend uf2 WHERE uf2.id_user=?) AND id_friend = ?"),
    USER_GET_BY_ID("SELECT * FROM users WHERE id_user=?"),
    FILM_GET_BY_ID("SELECT * FROM films WHERE id_film=?"),
    USER_UPDATE("UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id_user=?"),
    MPA_UPDATE("UPDATE mpa SET name=? WHERE id_mpa=?"),
    GENRE_UPDATE("UPDATE genres SET name=? WHERE id_genre=?"),
    ADD_FRIEND("INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)"),
    GET_FRIENDS_AS_USER("SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user= ?)"),
    GET_FRIENDS_AS_ID("SELECT id_friend FROM user_friend WHERE id_user=?"),
    DELETE_FRIEND("DELETE FROM user_friend WHERE id_user=? AND id_friend=?"),
    MPA_GET_BY_ID("SELECT id_mpa, name AS mpa_name FROM mpa WHERE id_mpa = ?"),
    GENRE_GET_BY_ID("SELECT * FROM genres WHERE id_genre = ?"),
    GENRES_GET_BY_FILM_ID(
        "SELECT * FROM genres WHERE id_genre IN (SELECT id_genre FROM film_genre WHERE id_film = ?) ORDER BY id_genre"),
    FILM_GENRE_SAVE("INSERT INTO film_genre (id_film, id_genre) VALUES (?,?)"),
    LIKE_ADD("INSERT INTO film_like (id_film, id_user) VALUES (?,?)"),
    RATE_GET_BY_FILM_ID("SELECT COUNT(DISTINCT id_user) FROM film_like WHERE id_film = ?"),
    FILM_UPDATE_FILMS("UPDATE films SET name=?, description=?, release_date=?, duration=?, id_mpa=? WHERE id_film=?"),
    FILM_GENRE_DELETE_BY_FILM_ID("DELETE FROM FILM_GENRE WHERE ID_FILM = ?"),
    LIKE_DELETE("DELETE FROM film_like WHERE id_film = ? AND id_user = ?"),
    GET_ALL_FILMS_GENRES(
            "SELECT fg.ID_FILM, g.ID_GENRE, g.NAME  FROM FILM_GENRE fg JOIN GENRES g ON fg.ID_GENRE = g.ID_GENRE"),
    GET_GENRES_BY_FILM_ID(
            "SELECT fg.ID_FILM, g.ID_GENRE, g.NAME FROM FILM_GENRE fg LEFT JOIN GENRES g ON fg.ID_GENRE = g.ID_GENRE " +
                    "WHERE fg.ID_FILM = ?"),
    GET_POPULAR_FILMS_WITHOUT_GENRES("SELECT " +
            "f.ID_FILM, " +
            "f.NAME film_name, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.ID_MPA, " +
            "m.NAME mpa_name, " +
            "COUNT(fl.ID_USER) rate FROM FILMS f \n" +
            "LEFT JOIN MPA m ON F.ID_MPA = M.ID_MPA " +
            "LEFT JOIN FILM_LIKE fl ON f.ID_FILM = fl.ID_FILM \n" +
            "GROUP BY f.ID_FILM, film_name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.ID_MPA, mpa_name " +
            "ORDER BY rate DESC LIMIT ?"),
    GET_FILMS_WITHOUT_GENRES("SELECT " +
            "f.ID_FILM, " +
            "f.NAME film_name, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.ID_MPA, " +
            "m.NAME mpa_name, " +
            "COUNT(fl.ID_USER) rate FROM FILMS f \n" +
            "LEFT JOIN MPA m ON F.ID_MPA = M.ID_MPA " +
            "LEFT JOIN FILM_LIKE fl ON f.ID_FILM = fl.ID_FILM \n" +
            "GROUP BY f.ID_FILM, film_name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.ID_MPA, mpa_name " +
            "ORDER BY ID_FILM"),
    GET_FILM_BY_ID_WITHOUT_GENRES("SELECT " +
            "f.ID_FILM, " +
            "f.NAME film_name, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.ID_MPA, " +
            "m.NAME mpa_name, " +
            "COUNT(fl.ID_USER) rate FROM FILMS f " +
            "LEFT JOIN MPA m ON F.ID_MPA = M.ID_MPA " +
            "LEFT JOIN FILM_LIKE fl ON f.ID_FILM = fl.ID_FILM " +
            "WHERE f.ID_FILM = ? " +
            "GROUP BY f.ID_FILM, film_name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.ID_MPA, mpa_name");

    final String query;

    DbQuery(String query) {
        this.query = query;
    }
}