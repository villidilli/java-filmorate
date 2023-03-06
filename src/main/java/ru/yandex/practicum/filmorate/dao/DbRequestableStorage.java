package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

public abstract class DbRequestableStorage<T extends Requestable> implements RequestableStorage<T> {

	public abstract List<T> getAll();

	public abstract T getById(Integer id);

	public abstract T update(T t);

	public abstract T add(T t);
}