package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.util.GenreIdComparator;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.RELEASE_DATE_INVALID;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class FilmService extends ServiceRequestable<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    public static final String PRIORITY_STORAGE = "InMemoryFilmStorage";
    private final UserService userService;
    private final FilmStorage storage;
    private final Comparator<Film> sortFilmByRate;

    @Autowired
    public FilmService(FilmStorage storage,
                       UserService userService) {
        this.storage = storage;
        this.userService = userService;
        sortFilmByRate = Comparator.comparing(Film::getRate).reversed();
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.deleteLike(filmId, userId);
//        log.debug(LOG_DELETE_LIKE.message, userId, filmId);
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        log.debug("/getPopularFilm");
        List<Film> films = getAllFilms();
        films.sort(sortFilmByRate);
        return films.stream().limit(countFilms).collect(Collectors.toList());
//        List<Film> films = sortFilms(popularDescComparator);
//        log.debug(LOG_POPULAR_FILMS.message, countFilms, films);
//        return films.stream().limit(countFilms).collect(Collectors.toList());
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("/getAll");
        List<Film> films = storage.getAllFilms();
        for (Film film : films) {
            film.setMpa(storage.getMpaById(film.getMpa().getId()));
            film.setGenres(storage.getFilmGenres(film.getId()));
            film.setRate(storage.getRateByFilmId(film.getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film, BindingResult bindResult) {
        log.debug("/create");
        customValidate(film);
        annotationValidate(bindResult);
        film.setId(storage.addFilmAndReturnId(film));
        film.setMpa(storage.getMpaById(film.getMpa().getId()));
        film.setGenres(storage.getGenresWithNameOnCreate(film));
        film.setRate(storage.getRateByFilmId(film.getId()));
        storage.addFilmGenres(film);
        return film;
    }

    @Override
    public Film update(Film film, BindingResult bindResult) {
        log.debug("/update");
        annotationValidate(bindResult);
        customValidate(film);
        isExist(film.getId());
        storage.updateFilms(film);
        storage.deleteFilmGenre(film);
        storage.addFilmGenres(film);
        return getById(film.getId());
    }

    @Override
    public Film getById(Integer filmId) {
        log.debug("/getById");
        isExist(filmId);
        Film film = storage.getFilmById(filmId);
        film.setGenres(storage.getFilmGenres(filmId));
        film.setMpa(storage.getMpaById(film.getMpa().getId()));
        film.setRate(storage.getRateByFilmId(filmId));
        return film;
    }

    @Override
    protected void customValidate(Film film) throws ValidateException {
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    protected void isExist(Integer id) {
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getFilmById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message, id);
    }

//    private List<Film> sortFilms(Comparator<Film> comparator) {
//        List<Film> list = storage.getAllFilms();
//        list.sort(comparator);
//        return list;
//    }
}