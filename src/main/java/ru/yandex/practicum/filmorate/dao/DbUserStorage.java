package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository("DbUserStorage")
public class DbUserStorage extends DbRequestableStorage<User> {
	private SimpleJdbcInsert jdbcInsert;
	private String sqlQuery;
	private Map<String, Object> params;


	@Autowired
	private DbUserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
	}

	@Override
	public List<User> getAll() {
		return null;
	}

	@Override
	public User getById(Integer id) {
		return null;
	}

	@Override
	public User update(User user) {
		return null;
	}

	@Override
	public User add(User user) {
		params.put("login", user.getLogin());
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("birthday", user.getBirthday());
		KeyHolder keyHolder = jdbcInsert
				.withTableName("users")
				.usingColumns("login", "name", "email", "birthday")
				.usingColumns("id_user")
				.withoutTableColumnMetaDataAccess()
				.executeAndReturnKeyHolder(params);
		user.setId(keyHolder.getKey().intValue());
		return user;
	}
}