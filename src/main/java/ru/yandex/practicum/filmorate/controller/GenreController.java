package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class GenreController extends ControllerRequestable<Genre>{
    @Override
    List<Genre> getAll() {
        return null;
    }

    @Override
    public Genre create(Genre obj, BindingResult bindResult) {
        return null;
    }

    @Override
    Genre update(Genre obj, BindingResult bindResult) {
        return null;
    }

    @Override
    Genre getById(Integer objId) {
        return null;
    }
}
