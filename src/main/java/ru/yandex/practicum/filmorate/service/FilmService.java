package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.RELEASE_DATE_INVALID;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class FilmService {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film, BindingResult bindResult) {
        customValidate(film);
        annotationValidate(bindResult);
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film, BindingResult bindResult) {
        annotationValidate(bindResult);
        customValidate(film);
        isExist(film.getId());
        filmStorage.update(film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        isExist(filmId);
        userService.isExist(userId);
        filmStorage.getById(filmId).getUserLikes().add(userId);
    }

    private void annotationValidate(BindingResult bindResult) throws ValidateException{
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
        log.debug(LOG_ANNOTATION_VALID_SUCCESS.message);
    }

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }

    private void customValidate(Film film) throws ValidateException{
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    private void isExist(Integer id) throws ValidateException, NotFoundException {
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (filmStorage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message);
    }
}