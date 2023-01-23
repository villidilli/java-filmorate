package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.RELEASE_DATE_INVALID;

@RestController
@Slf4j
@RequestMapping("/films")
@Validated
public class FilmController {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    private static int generatorID = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private void checkReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(BIRTHDAY_CINEMA)) throw new ValidationException(RELEASE_DATE_INVALID);
    }

    private void loggingChanges(Film film) {
        log.debug("Записан объект: {}", film);
        log.debug("Всего пользователей: [{}]", films.size());
        log.debug("ID generator [{}]", generatorID);
    }

    private void loggingException(Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] " + exception.getMessage());
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Всего пользователей: [{}]", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        try {
            checkReleaseDate(film);
            film.setId(generatorID++);
            films.put(film.getId(), film);
            loggingChanges(film);
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        try {
            checkReleaseDate(film);
            checkUserAvailability(user);
            films.put(film.getId(), film);
            loggingChanges(user);
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        } catch (NotFoundException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }
}
