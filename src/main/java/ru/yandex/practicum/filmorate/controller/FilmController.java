package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;

import java.time.LocalDate;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidateException.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    public static final LocalDate BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);
    protected final FilmService service;
    protected final FilmStorage storage;

    @Autowired
    public FilmController(FilmService service, FilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getAll() {
        return storage.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film create(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.create(obj, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film update(@Valid @RequestBody Film obj, BindingResult bindResult) {
        return super.update(obj, bindResult);
    }

    @Override
    protected void customValidate(Film obj) throws ValidateException {
        if (obj.getReleaseDate().isBefore(BIRTHDAY_CINEMA))
            throw new ValidateException("[ReleaseDate] -> " + RELEASE_DATE_INVALID);
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    protected Film addInStorage(Film obj) {
        return storage.add(obj);
    }

    @Override
    protected Film updateInStorage(Film obj) {
        return storage.update(obj);
    }
}