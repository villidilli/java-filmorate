package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

public abstract class DbRequestableStorage<T extends Requestable> implements RequestableStorage<T> {
	@Override //TODO реализовать методы
	public List<T> getAll() {
		return null;
	}

	@Override
	public T getById(Integer id) {
		return null;
	}

	@Override
	public T update(T t) {
		return null;
	}

	@Override
	public T add(T t) {
		return null;
	}
}