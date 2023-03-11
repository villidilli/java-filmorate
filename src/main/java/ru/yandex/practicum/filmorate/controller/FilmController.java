package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
public class FilmController extends ControllerRequestable<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    @GetMapping("/films")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getAll() {
        log.debug("/getAll");
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Film getById(@PathVariable("id") Integer filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/films/popular")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(value = "count",
                                                    required = false,
                                                    defaultValue = "10") Integer countFilms) {
        return filmService.getPopularFilms(countFilms);
    }

    @Override
    @PostMapping("/films")
    @ResponseStatus(value = HttpStatus.OK)
    public Film create(@Valid @RequestBody Film film, BindingResult bindResult) {
        log.debug("/create");
        return filmService.create(film, bindResult);
    }

    @Override
    @PutMapping("/films")
    @ResponseStatus(value = HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film, BindingResult bindResult) {
        log.debug("/update");
        log.debug("ПРИШЛО В КОНТРОЛЛЕР " + film.toString());
        return filmService.update(film, bindResult);
    }

    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("id") Integer filmId,
                        @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("id") Integer filmId,
                           @PathVariable Integer userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/mpa")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Mpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mpa getMpaById(@PathVariable("id") Integer mpaId) {
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/genres")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Genre getGenreById(@PathVariable("id") Integer genreId) {
        return filmService.getGenreById(genreId);
    }
}