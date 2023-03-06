package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryRequestableStorage;
import ru.yandex.practicum.filmorate.storage.RequestableStorage;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.ValidateException.RELEASE_DATE_INVALID;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class FilmService extends ServiceRequestable<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private final ServiceRequestable<User> userService;
    private final Comparator<Film> popularDescComparator;

    @Autowired
    public FilmService(@Qualifier("DbFilmStorage") RequestableStorage<Film> storage, ServiceRequestable<User> userService) {
        super.storage = storage;
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
    protected void customValidate(Film film) throws ValidateException {
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    private List<Film> sortFilms(Comparator<Film> comparator) {
        List<Film> list = storage.getAll();
        list.sort(comparator);
        return list;
    }
}