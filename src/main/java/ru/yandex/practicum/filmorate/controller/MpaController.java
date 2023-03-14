package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController extends ControllerRequestable<Mpa> {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.service = mpaService;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Mpa> getAll() {
        log.debug("/getAll");
        return service.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mpa create(@Valid @RequestBody Mpa mpa, BindingResult bindResult) {
        log.debug("/create");
        return service.create(mpa, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Mpa update(@Valid @RequestBody Mpa mpa, BindingResult bindResult) {
        log.debug("/update");
        return service.update(mpa, bindResult);
    }

    @Override
    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") Integer mpaId) {
        log.debug("/getById");
        return service.getById(mpaId);
    }
}