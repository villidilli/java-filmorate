package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController implements Controller<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film create(@Valid @RequestBody Film film, BindingResult bindResult) {
        return filmService.create(film, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film, BindingResult bindResult) {
        return filmService.update(film, bindResult);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable @Positive Integer id,
                        @PathVariable @Positive Integer userId) {
        filmService.addLike(id, userId);
    }

}