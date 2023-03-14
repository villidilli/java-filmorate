package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.dao.GenreStorage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;

@Service
@Slf4j
public class GenreService extends ServiceRequestable<Genre> {
    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Genre> getAll() {
        log.debug("/getAllGenres");
        return storage.getAll();
    }

    @Override
    public Genre create(Genre genre, BindingResult bindResult) {
        log.debug("/create");
        log.debug("income genre: " + genre);
        annotationValidate(bindResult);
        genre.setId(storage.addAndReturnId(genre));
        return genre;
    }

    @Override
    public Genre update(Genre genre, BindingResult bindResult) {
        log.debug("/update");
        log.debug("income genre: " + genre);
        isExist(genre.getId());
        storage.update(genre);
        return getById(genre.getId());
    }

    @Override
    public Genre getById(Integer genreId) {
        log.debug("/getById");
        isExist(genreId);
        return storage.getById(genreId);
    }

    public List<Genre> getGenresWithName(Film film) {
        log.debug("/getGenresWithNameFromFilm");
        return storage.getGenresWithName(film);
    }

    @Override
    protected void customValidate(Genre genre) {
        log.debug("/customValidate");
    }

    @Override
    protected void isExist(Integer id) {
        log.debug("/isExist(Genre)");
        log.debug("income genre id: " + id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}