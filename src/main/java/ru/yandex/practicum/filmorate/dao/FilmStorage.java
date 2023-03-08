package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmMapper;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.FILM_GET_ALL;
import static ru.yandex.practicum.filmorate.dao.DbQuery.USER_GET_ALL;

@Repository
@Slf4j
public class FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public FilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> getAll() {
        log.debug("/getAll");
        return jdbcTemplate.query(FILM_GET_ALL.query, new FilmMapper());
    }

    public Film getById(Integer id) {
        return null;
    }

    public Film update(Film film) {
        return null;
    }

    public Film add(Film film) {
        return null;
    }
}