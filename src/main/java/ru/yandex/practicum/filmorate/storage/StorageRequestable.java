package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

public interface StorageRequestable<T extends Requestable> {
    List<T> getAll();

    T add(T t);

    T update(T t);

    T getById(Integer id);
}