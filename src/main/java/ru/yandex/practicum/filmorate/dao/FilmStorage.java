package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
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
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final Comparator<Genre> sortGenreById;
    private final Comparator<Film> sortFilmById;

    @Autowired
    public FilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILMS_TABLE.query)
                .usingGeneratedKeyColumns(FILM_ID.query);
        sortGenreById = Comparator.comparing(Genre::getId);
        sortFilmById = Comparator.comparing(Film::getId);
    }

    public List<Film> getAllFilms() {
        log.debug("/getAll");
        List<Film> films = jdbcTemplate.query(FILM_GET_ALL.query, new FilmMapper());
        films.sort(sortFilmById);
        return films;
    }

    public void updateFilms(Film film) {
        jdbcTemplate.update(FILM_UPDATE_FILMS.query,
                            film.getName(),
                            film.getDescription(),
                            film.getReleaseDate(),
                            film.getDuration(),
                            film.getMpa().getId());
    }

    public void deleteFilmGenre(Film film) {
        jdbcTemplate.update(FILM_GENRE_DELETE_BY_FILM_ID.query, film.getId());
    }

    public Film getFilmById(Integer filmId) { //todo сделать исключения при null
        log.debug("/getFilmByID");
        return jdbcTemplate.query(FILM_GET_BY_ID.query, new FilmMapper(), filmId).stream()
                        .findAny().orElse(null);
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        jdbcTemplate.update(FILM_LIKE_SAVE.query, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        jdbcTemplate.update("DELETE FROM film_like WHERE id_film = ? AND id_user = ?", filmId, userId);
    }

    public Integer getRateByFilmId(Integer filmId) {
        log.debug("/getRateByFilmId");
        return jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, filmId);
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        List<Genre> list = jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), filmId);
        list.sort(sortGenreById);
        return list;
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

    public int addFilmAndReturnId(Film film) {
        log.debug("/addFilmAndReturnId");
        return jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
    }

    public List<Genre> getGenresWithNameOnCreate(Film film) {
        log.debug("/getGenresWithName");
        return film.getGenres().stream()
                .map(Genre::getId)
                .map(this::getGenreById)
                .sorted(new GenreIdComparator())
                .collect(Collectors.toList());
    }

    public void addFilmGenres(Film film) { //todo пакетное обновление
        film.getGenres().stream()
                .map(Genre::getId)
                .forEach(genreId -> jdbcTemplate.update(FILM_GENRE_SAVE.query, film.getId(), genreId));
    }

    private Genre getGenreById(Integer genreId) {
        return jdbcTemplate.queryForObject(GENRE_GET_BY_ID.query, new GenreMapper(), genreId);
    }

    public Mpa getMpaById(Integer mpaId) {
        return jdbcTemplate.queryForObject(MPA_GET_BY_ID.query, new MpaMapper(), mpaId);
    }
}