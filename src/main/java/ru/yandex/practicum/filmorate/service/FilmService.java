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
import ru.yandex.practicum.filmorate.model.Mpa;

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
    private final FilmStorage storage;
    private final Comparator<Film> sortFilmByRate;

    @Autowired
    public FilmService(FilmStorage storage,
                       UserService userService,
                       MpaService mpaService) {
        this.storage = storage;
        this.userService = userService;
        this.mpaService = mpaService;
        sortFilmByRate = Comparator.comparing(Film::getRate).reversed();
    }

    @Override
    public Film create(Film film, BindingResult bindResult) {
        log.debug("/create(Film)");
        log.debug("income film: " + film.toString());
        customValidate(film);
        annotationValidate(bindResult);
        film.setId(storage.addAndReturnId(film));
        film.setMpa(mpaService.getById(film.getMpa().getId()));
        film.setGenres(storage.getGenresWithNameOnCreate(film));
        film.setRate(storage.getRateByFilmId(film.getId()));
        storage.addFilmGenres(film);
        return film;
    }

    @Override
    public Film update(Film film, BindingResult bindResult) {
        log.debug("/update(Film)");
        log.debug("income film: " + film.toString());
        annotationValidate(bindResult);
        customValidate(film);
        isExist(film.getId());
        storage.deleteFilmGenre(film);
        storage.update(film);
        storage.addFilmGenres(film);
        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        log.debug("/getAll(Films)");
        List<Film> films = storage.getAll();
        for (Film film : films) {
            film.setMpa(mpaService.getById(film.getMpa().getId()));
            film.setGenres(storage.getFilmGenres(film.getId()));
            film.setRate(storage.getRateByFilmId(film.getId()));
        }
        return films;
    }

    @Override
    public Film getById(Integer filmId) {
        log.debug("/getById(Film)");
        log.debug("income film id: " + filmId);
        isExist(filmId);
        Film film = storage.getById(filmId);
        log.debug("return from db film: " + film.toString());
        film.setGenres(storage.getFilmGenres(filmId));
        film.setMpa(mpaService.getById(film.getMpa().getId()));
        film.setRate(storage.getRateByFilmId(filmId));
        return film;
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        log.debug("/getPopularFilm");
        List<Film> films = getAll();
        films.sort(sortFilmByRate);
        return films.stream().limit(countFilms).collect(Collectors.toList());
    }

//    public List<Mpa> getAllMpa() { //TODO вынес
//        log.debug("/getAllMpa");
//        return storage.getAllMpa();
//    }

//    public Mpa getMpaById(Integer mpaId) { //TODO вынес
//        log.debug("/getMpaById");
//        log.debug("income mpa id: " + mpaId);
//        if (mpaId == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
//        return storage.getMpaById(mpaId);
//    }

    public List<Genre> getAllGenres() {
        log.debug("/getAllGenres");
        return storage.getAllGenres();
    }

    public Genre getGenreById(Integer genreId) {
        log.debug("/getGenreById");
        log.debug("income genre id: " + genreId);
        if (genreId == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        return storage.getGenreById(genreId);
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
        log.debug("income film id: " + id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}