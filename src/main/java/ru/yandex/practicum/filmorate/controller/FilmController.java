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
public class FilmController implements Controller<Film> {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getAll() {
        return service.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film create(@Valid @RequestBody Film film, BindingResult bindResult) {
        return service.create(film, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film, BindingResult bindResult) {
        return service.update(film, bindResult);
    }
}