package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.util.GenreMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;

@Repository
@Slf4j
public class GenreStorage implements RequestableStorage<Genre> {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final Comparator<Genre> sortGenreById;

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        sortGenreById = Comparator.comparing(Genre::getId);
    }

    @Override
    public List<Genre> getAll() {
        log.debug("/getAllGenres");
        try {
            return jdbcTemplate.query(GENRES_GET_ALL.query, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getById(Integer genreId) {
        log.debug("/getById");
        log.debug("income id: {}", genreId);
        try {
            return jdbcTemplate.queryForObject(GENRE_GET_BY_ID.query, new GenreMapper(), genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + genreId + "]" + NOT_FOUND_BY_ID);
        }
    }

    @Override
    public int addAndReturnId(Genre genre) {
        log.debug("/addGenreAndReturnId");
        log.debug("income genre: {}", genre.toString());
        return jdbcInsert.executeAndReturnKey(Map.of("name", genre.getName())).intValue();
    }

    @Override
    public void update(Genre genre) {
        log.debug("/update");
        log.debug("income genre: {}", genre);
        jdbcTemplate.update(MPA_UPDATE.query, genre.getId());
    }
}