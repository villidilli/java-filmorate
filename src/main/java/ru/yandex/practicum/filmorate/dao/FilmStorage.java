package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.util.*;

import java.util.*;

import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class FilmStorage implements RequestableStorage<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public FilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILMS_TABLE.query)
                .usingGeneratedKeyColumns(FILM_ID.query);
    }

    @Override
    public int addAndReturnId(Film film) {
        log.debug("/addFilmAndReturnId");
        log.debug("income film: " + film);
        return jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
    }

    @Override
    public void update(Film film) {
        log.debug("/updateFilm");
        jdbcTemplate.update(FILM_UPDATE_FILMS.query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public List<Film> getAll() {
        log.debug("/getAllFilms");
        return jdbcTemplate.query(FILM_GET_ALL.query, new FilmMapper());
    }

    @Override
    public Film getById(Integer filmId) {
        log.debug("/getFilmByID");
        return jdbcTemplate.query(FILM_GET_BY_ID.query, new FilmMapper(), filmId).stream()
                .findAny()
                .orElse(null);
    }

    private Map<String, Object> convertFilmToRow(Film film) {
        log.debug("/convertFilmToRow");
        Map<String, Object> param = new HashMap<>();
        param.put("name", film.getName());
        param.put("description", film.getDescription());
        param.put("release_date", film.getReleaseDate());
        param.put("duration", film.getDuration());
        param.put("id_mpa", film.getMpa().getId());
        return param;
    }
}