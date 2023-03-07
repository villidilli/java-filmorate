package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

public abstract class RequestableStorage<T extends Requestable>  {
	protected JdbcTemplate jdbcTemplate;
	protected SimpleJdbcInsert jdbcInsert;
	protected String tableName;
	protected String nameIdColumn;
	protected String queryGetAll;
	protected String queryGetById;
	protected String queryUpdate;
	protected String queryGetObjById;
	protected String queryAddFriend;


	public abstract List<T> getAll();

	public abstract T getById(Integer id);

	public abstract T update(T t);

	public abstract T add(T t);

	public abstract void addFriend(int id, int friendId);
}