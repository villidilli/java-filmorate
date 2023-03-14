package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.util.GenreMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FilmGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    public void addFilmGenres(Film film) {
        log.debug("/addFIlmGenres");
        new ArrayList<>(new HashSet<>(film.getGenres())).stream()
                .map(Genre::getId)
                .forEach(genreId -> jdbcTemplate.update(FILM_GENRE_SAVE.query, film.getId(), genreId));
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        log.debug("/getFilmGenres");
        return jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), filmId);
    }

    public void deleteFilmGenre(Film film) {
        log.debug("/deleteFilmGenre");
        jdbcTemplate.update(FILM_GENRE_DELETE_BY_FILM_ID.query, film.getId());
    }
}
