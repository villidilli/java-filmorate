package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

public interface RequestableStorage<T extends Requestable> {
	List<T> getAll();
	T getById(Integer id);
	T update(T t);
	T add(T t);
}