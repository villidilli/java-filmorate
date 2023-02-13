package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageRequestable;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.yandex.practicum.filmorate.exception.ValidateException.RELEASE_DATE_INVALID;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class FilmService extends ServiceRequestable<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private final ServiceRequestable<User> userService;

    @Autowired
    public FilmService(StorageRequestable<Film> storage, ServiceRequestable<User> userService) {
        super.storage = storage;
        this.userService = userService;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.debug("/addLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).getUserLikes().add(userId);
        log.debug(LOG_ADD_LIKE.message, userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("/deleteLike");
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).getUserLikes().remove(userId);
        log.debug(LOG_DELETE_LIKE.message, userId, filmId);
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        log.debug("/getPopularFilm");
        List<Film> result;
        Stream<Film> sortedFilms = storage.getAll().stream()
                .sorted((o1, o2) -> o2.getUserLikes().size() - o1.getUserLikes().size());
        result = sortedFilms.limit(countFilms).collect(Collectors.toList());
        log.debug(LOG_POPULAR_FILMS.message, countFilms, result.stream().mapToInt(Film::getId).toArray());
        return result;
    }

    @Override
    protected void customValidate(Film film) throws ValidateException {
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }
}