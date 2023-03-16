package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.util.*;

import java.util.*;

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
        log.debug("/update");
        jdbcTemplate.update(FILM_UPDATE_FILMS.query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    public List<Film> getPopularFilms(int countFilms) {
        log.debug("/getPopularFilms");
        Map<Integer, List<Genre>> allFilmsIdWithGenres =
                jdbcTemplate.query(GET_ALL_FILMS_GENRES.query, new GenresExtractor());
        List<Film> filmsWithoutGenres =
                jdbcTemplate.query(GET_POPULAR_FILMS_WITHOUT_GENRES.query, new FilmMapper(), countFilms);
        return collectGenresToFilm(allFilmsIdWithGenres, filmsWithoutGenres);
    }

    @Override
    public List<Film> getAll() {
        log.debug("/getAllFilms");
        Map<Integer, List<Genre>> allFilmsIdWithGenres =
                jdbcTemplate.query(GET_ALL_FILMS_GENRES.query, new GenresExtractor());
        List<Film> filmsWithoutGenres = jdbcTemplate.query(GET_FILMS_WITHOUT_GENRES.query, new FilmMapper());
        return collectGenresToFilm(allFilmsIdWithGenres, filmsWithoutGenres);
    }

    private List<Film> collectGenresToFilm(Map<Integer, List<Genre>> allFilmsIdWithGenres, List<Film> films) {
        log.debug("/collectGenresToFilm");
        films.iterator().forEachRemaining(film -> {
            List<Genre> genres = allFilmsIdWithGenres.get(film.getId());
            if (genres != null) film.setGenres(genres);
        });
        return films;
    }

    @Override
    public Film getById(Integer filmId) {
        log.debug("/getFilmByID");
        Map<Integer, List<Genre>> allFilmsIdWithGenres =
                jdbcTemplate.query(GET_GENRES_BY_FILM_ID.query, new GenresExtractor(), filmId);
        List<Film> film = jdbcTemplate.query(GET_FILM_BY_ID_WITHOUT_GENRES.query, new FilmMapper(), filmId);
        return collectGenresToFilm(allFilmsIdWithGenres, film).get(0);
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

    public boolean isExist(Integer filmId) {
        log.debug("/isExist");
        try {
            jdbcTemplate.queryForMap("SELECT id_film FROM films WHERE id_film = ?", filmId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }
}