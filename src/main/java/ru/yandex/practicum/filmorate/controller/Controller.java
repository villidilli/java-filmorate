package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.*;

public interface Controller<T extends Requestable> {
    List<T> getAll();
    T create(T obj, BindingResult bindResult);
    T update(T obj, BindingResult bindResult);
}