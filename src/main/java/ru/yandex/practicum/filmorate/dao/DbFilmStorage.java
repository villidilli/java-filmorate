package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository("DbFilmStorage")
public class DbFilmStorage extends DbRequestableStorage<Film>{
    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public Film getById(Integer id) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film add(Film film) {
        return null;
    }
}