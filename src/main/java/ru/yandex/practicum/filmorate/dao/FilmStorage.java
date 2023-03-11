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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.*;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;

@Repository
@Slf4j
public class FilmStorage implements RequestableStorage<Film>{
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

    public Integer getRateByFilmId(Integer filmId) {
        log.debug("/getRateByFilmId");
        return jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, filmId);
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        log.debug("/getFilmGenres");
        return jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), filmId);
    }

    public List<Genre> getGenresWithNameOnCreate(Film film) {
        log.debug("/getGenresWithNameOnCreate");
        return film.getGenres().stream()
                .map(Genre::getId)
                .map(this::getGenreById)
                .sorted(new GenreIdComparator())
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenres() {
        log.debug("/getAllGenres");
        try {
            return jdbcTemplate.query(GENRES_GET_ALL.query, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Genre getGenreById(Integer genreId) {
        log.debug("/getGenreById");
        try {
            return jdbcTemplate.queryForObject(GENRE_GET_BY_ID.query, new GenreMapper(), genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + genreId + "]" + NOT_FOUND_BY_ID);
        }
    }

    public List<Mpa> getAllMpa() {
        log.debug("/getAllMpa");
        try {
            return jdbcTemplate.query(MPA_GET_ALL.query, new MpaMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Mpa getMpaById(Integer mpaId) throws NotFoundException{
        log.debug("/getMpaById");
        try {
            return jdbcTemplate.queryForObject(MPA_GET_BY_ID.query, new MpaMapper(), mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + mpaId + "]" + NOT_FOUND_BY_ID);
        }
    }

    public void deleteFilmGenre(Film film) {
        log.debug("/deleteFilmGenre");
        jdbcTemplate.update(FILM_GENRE_DELETE_BY_FILM_ID.query, film.getId());
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        jdbcTemplate.update(LIKE_DELETE.query, filmId, userId);
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        jdbcTemplate.update(LIKE_ADD.query, filmId, userId);
    }

    public void addFilmGenres(Film film) {
        log.debug("/addFIlmGenres");
        new ArrayList<>(new HashSet<>(film.getGenres())).stream()
                .map(Genre::getId)
                .forEach(genreId -> jdbcTemplate.update(FILM_GENRE_SAVE.query, film.getId(), genreId));
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