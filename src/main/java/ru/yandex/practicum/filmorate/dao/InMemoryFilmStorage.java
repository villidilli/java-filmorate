package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@Repository("InMemoryFilmStorage")
public class InMemoryFilmStorage extends InMemoryRequestableStorage<Film> {
}