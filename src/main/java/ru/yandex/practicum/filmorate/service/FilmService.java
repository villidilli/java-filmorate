package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
    public static final Integer DEFAULT_NUM_POPULAR_FILMS = 10;

    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        super.storage = storage;
        this.userService = userService;
    }

    public void addLike(Integer filmId, Integer userId) {
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).getUserLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        isExist(filmId);
        userService.isExist(userId);
        storage.getById(filmId).getUserLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        Stream<Film> sortedFilms = storage.getAll().stream().sorted((o1, o2) -> o2.getUserLikes().size()-o1.getUserLikes().size());
        if(countFilms == null) {
          return sortedFilms.limit(DEFAULT_NUM_POPULAR_FILMS).collect(Collectors.toList());
        }
        return sortedFilms.limit(countFilms).collect(Collectors.toList());
    }

    @Override
    protected void customValidate(Film film) throws ValidateException{
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }
}