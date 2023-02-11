package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.service.ServiceRequestable;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

public interface ControllerRequestable<T extends Requestable> {
    List<T> getAll();
    T create(T obj, BindingResult bindResult);
    T update(T obj, BindingResult bindResult);
}