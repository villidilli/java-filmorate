package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Requestable;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationException.*;

@RestController
@Slf4j
@RequestMapping("/films")
@Validated
public class FilmController extends Controller<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);

    @Override
    protected void validate(Film obj) throws ValidationException {
        if (obj.getReleaseDate().isBefore(BIRTHDAY_CINEMA)) throw new ValidationException(RELEASE_DATE_INVALID);
    }

    @Override
    @GetMapping
    public List<Requestable> getAllObjects() {
        return super.getAllObjects();
    }

    @Override
    @PostMapping
    public ResponseEntity<Requestable> create(@Valid @RequestBody Film obj) {
        return super.create(obj);
    }

    @Override
    @PutMapping
    public ResponseEntity<Requestable> update(@Valid @RequestBody Film obj) {
        return super.update(obj);
    }
}