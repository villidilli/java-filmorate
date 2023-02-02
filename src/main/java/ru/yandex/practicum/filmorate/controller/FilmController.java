package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Requestable;

import javax.validation.Valid;

import java.time.LocalDate;

import java.util.List;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidateException.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);

    @Override
    protected void customValidate(Film obj) throws ValidateException {
        if (obj.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Requestable> getAllObjects() {
        return super.getAllObjects();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Requestable create(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.create(obj, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Requestable update(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.update(obj, bindResult);
    }
}