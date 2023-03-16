package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.dao.MpaStorage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;

@Service
@Slf4j
public class MpaService extends ServiceRequestable<Mpa> {
    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Mpa> getAll() {
        log.debug("/getAll");
        return storage.getAll();
    }

    @Override
    public Mpa create(Mpa mpa, BindingResult bindResult) {
        log.debug("/create");
        log.debug("income mpa: {}", mpa);
        annotationValidate(bindResult);
        mpa.setId(storage.addAndReturnId(mpa));
        return storage.getById(mpa.getId());
    }

    @Override
    public Mpa update(Mpa mpa, BindingResult bindResult) {
        log.debug("/update");
        log.debug("income mpa: {}", mpa);
        isExist(mpa.getId());
        storage.update(mpa);
        return getById(mpa.getId());
    }

    @Override
    public Mpa getById(Integer mpaId) {
        log.debug("/getById");
        log.debug("mpaId: {}", mpaId);
        isExist(mpaId);
        return storage.getById(mpaId);
    }

    @Override
    protected void customValidate(Mpa mpa) {}

    @Override
    protected void isExist(Integer id) {
        log.debug("/isExist(Mpa)");
        log.debug("income mpa id: {}", id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}