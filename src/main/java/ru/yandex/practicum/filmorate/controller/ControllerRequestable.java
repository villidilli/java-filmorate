package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.*;

public abstract class ControllerRequestable<T extends Requestable> {
    abstract List<T> getAll();

    abstract T create(T obj, BindingResult bindResult);

    abstract T update(T obj, BindingResult bindResult);

    abstract T getById(Integer objId);
}