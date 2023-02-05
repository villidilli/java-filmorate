package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.util.Message.LOG_IS_EXIST_SUCCESS;
import static ru.yandex.practicum.filmorate.util.Message.LOG_WRITE_OBJECT;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    protected final Map<Integer, Film> films = new HashMap<>();
    protected Integer generatorId = 1;
    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film) {
        film.setId(generatorId++);
        films.put(film.getId(), film);
        log.debug(LOG_WRITE_OBJECT.message, film.getClass().getSimpleName());
        return film;
    }

    @Override
    public Film update(Film film) {
        isExist(film);
        films.put(film.getId(), film);
        log.debug(LOG_WRITE_OBJECT.message, film.getClass().getSimpleName());
        return film;
    }

    private void isExist(Film film) {
        Integer id = film.getId();
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (films.get(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message);
    }
}
