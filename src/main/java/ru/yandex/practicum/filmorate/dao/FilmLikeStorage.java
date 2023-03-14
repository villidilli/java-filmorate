package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class FilmLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        jdbcTemplate.update(LIKE_ADD.query, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        jdbcTemplate.update(LIKE_DELETE.query, filmId, userId);
    }

    public Integer getRateByFilmId(Integer filmId) {
        log.debug("/getRateByFilmId");
        return jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, filmId);
    }

}
