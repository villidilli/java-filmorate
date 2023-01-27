package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;

@RestController
@Slf4j
@RequestMapping("/films")
@Validated
public class FilmController {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorID = 1;

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA)) throw new ValidationException(RELEASE_DATE_INVALID);
    }

    private void isFilmExist(Film film) throws ValidationException {
        Integer id = film.getId();
        if (id == null) throw new ValidationException(ID_NOT_IS_BLANK);
        if (films.get(id) == null) throw new ValidationException(NOT_FOUND);
    }

    private void logVariablesCondition() {
        log.info(LOG_SIZE_FILMS.message, films.size());
        log.debug(LOG_ID_GEN.message, generatorID);
    }

    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }

    @GetMapping
    public List<Film> getAllFilms() {
        logVariablesCondition();
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        try {
            validate(film);
            log.debug(LOG_VALIDATION_SUCCESS.message);
            film.setId(generatorID++);
            films.put(film.getId(), film);
            log.debug(LOG_WRITE_OBJECT.message, film);
            logVariablesCondition();
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            logException(HttpStatus.BAD_REQUEST, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        try {
            validate(film);
            isFilmExist(film);
            log.debug(LOG_VALIDATION_SUCCESS.message);
            films.put(film.getId(), film);
            log.debug(LOG_WRITE_OBJECT.message, film);
            logVariablesCondition();
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            logException(HttpStatus.NOT_FOUND, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }
}