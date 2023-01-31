package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Requestable;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.*;

@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);

    @Override
    protected void validate(Film obj) throws ValidationException {
        if (obj.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidationException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
    }

    @Override
    @GetMapping
    public List<Requestable> getAllObjects() {
        return super.getAllObjects();
    }

    @Override
    @PostMapping
    public ResponseEntity<Requestable> create(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.create(obj, bindResult);
    }

    @Override
    @PutMapping
    public ResponseEntity<Requestable> update(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.update(obj, bindResult);
    }

    @Override
    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> exceptionHandler(ValidationException e) {
        return super.exceptionHandler(e);
    }

    @Override
    @ExceptionHandler
    protected ResponseEntity<ExceptionResponse> exceptionHandler(NotFoundException e) {
        return super.exceptionHandler(e);
    }
}