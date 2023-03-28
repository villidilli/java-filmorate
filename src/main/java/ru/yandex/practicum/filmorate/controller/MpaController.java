package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.service = mpaService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Mpa> getAll() {
        log.debug("/getAll");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") Integer mpaId) {
        log.debug("/getById");
        return service.getById(mpaId);
    }
}