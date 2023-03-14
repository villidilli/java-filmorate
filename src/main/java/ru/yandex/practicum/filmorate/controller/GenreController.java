package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController extends ControllerRequestable<Genre> {
    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Genre> getAll() {
        log.debug("/getAll");
        return service.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Genre create(@Valid @RequestBody Genre genre, BindingResult bindResult) {
        log.debug("/create");
        return service.create(genre, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Genre update(@Valid @RequestBody Genre genre, BindingResult bindResult) {
        log.debug("/update");
        return service.update(genre, bindResult);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Genre getById(@PathVariable("id") Integer genreId) {
        log.debug("/getById");
        return service.getById(genreId);
    }
}