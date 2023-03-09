package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public Film add(Film film) {
        log.debug("/add");
        log.debug(film.toString());
        updateFilmId(film);
        updateFilmMpa(film);
        updateGenres(film);
        addFilmGenresToDB(film);
        return film;
    }

    public Film update(Film film) {
        return null;
    }

    public Film getById(Integer id) { //todo сделать исключения при null
        log.debug("/getByID");
        Film film = getFilm(id);
        updateFilmGenresFromDB(film);
        updateFilmMpa(film);
        updateFilmRate(film);

        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        jdbcTemplate.update(FILM_LIKE_SAVE.query, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        jdbcTemplate.update("DELETE FROM film_like WHERE id_film = ? AND id_user = ?", filmId, userId);
    }

    private void updateFilmRate(Film film) {
        log.debug("/updateFilmRate");
        Integer rate = jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, film.getId());
        film.setRate(rate);
    }

    private void updateFilmGenresFromDB(Film film) {
        List<Genre> genres = jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), film.getId());
        film.setGenres(genres);
    }

    private Film getFilm(Integer filmId) {
        log.debug("/getFilm");
        return jdbcTemplate.query(FILM_GET_BY_ID.query, new FilmMapper(), filmId).stream()
                        .findAny().orElse(null);
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

    private void updateFilmId(Film film) {
        int filmId = jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
        film.setId(filmId);
    }

    private void updateFilmMpa(Film film) {
        log.debug("/saveMpa");
        Mpa mpa = getMpaById(film.getMpa().getId());
        film.setMpa(mpa);
    }

    private void updateGenres(Film film) {
        log.debug("/saveGenres");
        List<Genre> genres = film.getGenres().stream()
                .map(Genre::getId)
                .map(this::getGenreById)
                .sorted(new GenreIdComparator())
                .collect(Collectors.toList());
        film.setGenres(genres);
    }

    private void addFilmGenresToDB(Film film) {
        film.getGenres().stream()
                .map(Genre::getId)
                .forEach(genreId -> jdbcTemplate.update(FILM_GENRE_SAVE.query, film.getId(), genreId));
    }


    private Genre getGenreById(Integer genreId) {
        return jdbcTemplate.queryForObject(GENRE_GET_BY_ID.query, new GenreMapper(), genreId);
    }

    private Mpa getMpaById(Integer mpaId) {
        return jdbcTemplate.queryForObject(MPA_GET_BY_ID.query, new MpaMapper(), mpaId);
    }


}