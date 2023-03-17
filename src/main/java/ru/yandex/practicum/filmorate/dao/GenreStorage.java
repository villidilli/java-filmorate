package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.util.GenreMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;

@Repository
@Slf4j
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAll() {
        log.debug("/getAllGenres");
        try {
            return jdbcTemplate.query(GENRES_GET_ALL.query, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Genre getById(Integer genreId) {
        log.debug("/getById");
        log.debug("income id: {}", genreId);
        try {
            return jdbcTemplate.queryForObject(GENRE_GET_BY_ID.query, new GenreMapper(), genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + genreId + "]" + NOT_FOUND_BY_ID);
        }
    }
}