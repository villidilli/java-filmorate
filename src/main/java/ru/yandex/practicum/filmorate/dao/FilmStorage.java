package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.FilmMapper;
import ru.yandex.practicum.filmorate.util.MpaMapper;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public FilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILMS_TABLE.query)
                .usingGeneratedKeyColumns(FILM_ID.query);
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
        log.debug("/add");
        log.debug(film.toString());//todo delete
        int filmId = jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
        Mpa mpa = getMpa(film.getMpa().getId());
        film.setId(filmId);
        film.setMpa(mpa);
        return film;
    }

    private Map<String, Object> convertFilmToRow(Film film) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", film.getName());
        param.put("description", film.getDescription());
        param.put("release_date", film.getReleaseDate());
        param.put("duration", film.getDuration());
        param.put("id_mpa", film.getMpa().getId());
        return param;
    }

    private int getGeneratedKey(Film film) {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILMS_TABLE.query)
                .usingGeneratedKeyColumns(FILM_ID.query);
        return jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(film)).intValue();
    }

    private void updateFilmMpaId (Integer mpaId, Integer filmId) {
        jdbcTemplate.update(FILM_UPDATE_ID_MPA.query, mpaId, filmId);
    }

    private Mpa getMpa(Integer mpaId) {
        return jdbcTemplate.queryForObject(MPA_GET_NAME_BY_ID.query, new MpaMapper(), mpaId);
    }

}