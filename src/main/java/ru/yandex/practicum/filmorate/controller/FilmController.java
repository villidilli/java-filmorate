package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController implements ControllerRequestable<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getAll() {
        log.debug("/getAll");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Film getById(@PathVariable("id") Integer filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false) Integer countFilms) {
        return filmService.getPopularFilms(countFilms);
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film create(@Valid @RequestBody Film film, BindingResult bindResult) {
        log.debug("/create");
        return filmService.create(film, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film, BindingResult bindResult) {
        log.debug("/update");
        return filmService.update(film, bindResult);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("id") Integer filmId,
                        @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("id") Integer filmId,
                           @PathVariable Integer userId) {
        filmService.deleteLike(filmId, userId);
    }
}