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

    public List<Film> getAllFilms() {
        log.debug("/getAll");
        List<Film> films = jdbcTemplate.query(FILM_GET_ALL.query, new FilmMapper());
        films.sort(new FilmIdComparator().reversed()); //todo тестирую reversed
        return films;
    }

//    public Film add(Film film) {
//        log.debug("/add");
////        updateFilmId(film);
////        updateFilmMpa(film);
////        updateGenres(film);
////        addFilmGenresToDB(film);
////        return film;
//    }

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


    public Film update(Film film) {
        return null;
    }

    public Film getFilmById(Integer filmId) { //todo сделать исключения при null
        log.debug("/getFilmByID");
        return jdbcTemplate.query(FILM_GET_BY_ID.query, new FilmMapper(), filmId).stream()
                        .findAny().orElse(null);
//        Film film = getFilmById(id);
//        updateFilmGenresFromDB(film);
//        updateFilmMpa(film);
//        updateFilmRate(film);

//        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        jdbcTemplate.update(FILM_LIKE_SAVE.query, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        jdbcTemplate.update("DELETE FROM film_like WHERE id_film = ? AND id_user = ?", filmId, userId);
    }

//    private void updateFilmRate(Film film) {
//        log.debug("/updateFilmRate");
//        Integer rate = jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, film.getId());
//        film.setRate(rate);
//    }

    public Integer getRateByFilmId(Integer filmId) {
        log.debug("/getRateByFilmId");
        return jdbcTemplate.queryForObject(RATE_GET_BY_FILM_ID.query, Integer.class, filmId);
    }

//    private void updateFilmGenresFromDB(Film film) {
//        List<Genre> genres = jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), film.getId());
//        film.setGenres(genres);
//    }

    public List<Genre> getFilmGenres(Integer filmId) {
        List<Genre> list = jdbcTemplate.query(GENRES_GET_BY_FILM_ID.query, new GenreMapper(), filmId);
        list.sort(new GenreIdComparator());
        return list;
    }

//    public Film getFilmById(Integer filmId) {
//        log.debug("/getFilmById");
//        return jdbcTemplate.query(FILM_GET_BY_ID.query, new FilmMapper(), filmId).stream()
//                        .findAny().orElse(null);
//    }

    private Map<String, Object> convertFilmToRow(Film film) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", film.getName());
        param.put("description", film.getDescription());
        param.put("release_date", film.getReleaseDate());
        param.put("duration", film.getDuration());
        param.put("id_mpa", film.getMpa().getId());
        return param;
    }

//    private void updateFilmId(Film film) {
//        int filmId = jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
//        film.setId(filmId);
//    }

    public int addFilmAndReturnId(Film film) {
        log.debug("/addFilmAndReturnId");
        return jdbcInsert.executeAndReturnKey(convertFilmToRow(film)).intValue();
    }

//    private void updateFilmMpa(Film film) {
//        log.debug("/saveMpa");
//        Mpa mpa = getMpaById(film.getMpa().getId());
//        film.setMpa(mpa);
//    }

//    public Mpa getMpaWithName(Film film) {
//        log.debug("/getMpaWithName");
//        return getMpaById(film.getMpa().getId());
//    }

//    private void updateGenres(Film film) {
//        log.debug("/saveGenres");
//        List<Genre> genres = film.getGenres().stream()
//                .map(Genre::getId)
//                .map(this::getGenreById)
//                .sorted(new GenreIdComparator())
//                .collect(Collectors.toList());
//        film.setGenres(genres);
//    }

    public List<Genre> getGenresWithNameOnCreate(Film film) {
        log.debug("/getGenresWithName");
        return film.getGenres().stream()
                .map(Genre::getId)
                .map(this::getGenreById)
                .sorted(new GenreIdComparator())
                .collect(Collectors.toList());
    }

//    private void addFilmGenresToDB(Film film) {
//        film.getGenres().stream()
//                .map(Genre::getId)
//                .forEach(genreId -> jdbcTemplate.update(FILM_GENRE_SAVE.query, film.getId(), genreId));
//    }

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