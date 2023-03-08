package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

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
    private final Comparator<Film> popularDescComparator;
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage,
                       UserService userService) {
        this.storage = storage;
        this.userService = userService;
        popularDescComparator = Comparator.comparing(Film::getCountUserlikes).reversed();
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).addLike(userId);
        log.debug(LOG_ADD_LIKE.message, userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).deleteLike(userId);
        log.debug(LOG_DELETE_LIKE.message, userId, filmId);
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        log.debug("/getPopularFilm");
        List<Film> films = sortFilms(popularDescComparator);
        log.debug(LOG_POPULAR_FILMS.message, countFilms, films);
        return films.stream().limit(countFilms).collect(Collectors.toList());
    }

    @Override
    public List<Film> getAll() {
        log.debug("/getAll");
        return storage.getAll();
    }

    @Override
    public Film create(Film film, BindingResult bindResult) {
        log.debug("/create");
        customValidate(film);
        annotationValidate(bindResult);
        storage.add(film);
        return film;
    }

    @Override
    public Film update(Film film, BindingResult bindResult) {
        log.debug("/update");
        annotationValidate(bindResult);
        customValidate(film);
        isExist(film.getId());
        storage.update(film);
        return film;
    }

    @Override
    public Film getById(Integer id) {
        log.debug("/getById");
        isExist(id);
        return storage.getById(id);
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
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message, id);
    }

    private List<Film> sortFilms(Comparator<Film> comparator) {
        List<Film> list = storage.getAll();
        list.sort(comparator);
        return list;
    }
}