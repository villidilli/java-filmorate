package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Message.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements StorageRequestable<Film> {
    protected final Map<Integer, Film> films = new HashMap<>();
    protected Integer generatorId = 1;

    @Override
    public List<Film> getAll() {
        log.debug(LOG_SIZE_OBJECTS.message, films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film) {
        film.setId(generatorId++);
        films.put(film.getId(), film);
        log.debug(LOG_WRITE_OBJECT.message, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug(LOG_UPDATE_OBJECT.message, film);
        return film;
    }

    @Override
    public Film getById(Integer id) {
        return films.get(id);
    }
}