package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import ru.yandex.practicum.filmorate.dao.FilmLikeStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.RELEASE_DATE_INVALID;

@Service
@Slf4j
public class FilmService extends ServiceRequestable<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final FilmGenreStorage filmGenreStorage;
    private final FilmStorage storage;
    private final FilmLikeStorage likeStorage;
    private final Comparator<Film> sortFilmByRate;

    @Autowired
    public FilmService(FilmStorage storage,
                       UserService userService,
                       MpaService mpaService,
                       GenreService genreService,
                       FilmGenreStorage filmGenreStorage,
                       FilmLikeStorage filmLikeStorage) {
        this.storage = storage;
        this.userService = userService;
        this.mpaService = mpaService;
        this.genreService = genreService;
        this.filmGenreStorage = filmGenreStorage;
        this.likeStorage = filmLikeStorage;
        sortFilmByRate = Comparator.comparing(Film::getRate).reversed();
    }

    @Override
    public Film create(Film film, BindingResult bindResult) {
        log.debug("/create(Film)");
        log.debug("income film: {}", film.toString());
        customValidate(film);
        annotationValidate(bindResult);
        int filmId = storage.addAndReturnId(film);
        film.setId(filmId);
        filmGenreStorage.addFilmGenres(film);
        return storage.getById(filmId);
    }

    @Override
    public Film update(Film film, BindingResult bindResult) {
        log.debug("/update(Film)");
        log.debug("income film: {}", film.toString());
        annotationValidate(bindResult);
        customValidate(film);
        isExist(film.getId());
        filmGenreStorage.deleteFilmGenre(film);
        storage.update(film);
        filmGenreStorage.addFilmGenres(film);
        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        log.debug("/getAll(Films)");
        return storage.getAll();
    }

    @Override
    public Film getById(Integer filmId) {
        log.debug("/getById(Film)");
        log.debug("income film id: {}", filmId);
        isExist(filmId);
        return storage.getById(filmId);
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        log.debug("/getPopularFilm");
//        List<Film> films = getAll();
        return storage.getPopularFilms(countFilms);
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        isExist(filmId);
        userService.isExist(userId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        isExist(filmId);
        userService.isExist(userId);
        likeStorage.deleteLike(filmId, userId);
    }

    @Override
    protected void customValidate(Film film) throws ValidateException {
        log.debug("customValidate(film)");
        log.debug("income film: " + film.toString());
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
    }

    @Override
    protected void isExist(Integer id) {
        log.debug("/isExist(Film)");
        log.debug("income film id: {}", id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (!storage.isExist(id)) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
//        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}