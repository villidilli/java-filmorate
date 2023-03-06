package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository("DbUserStorage")
@Slf4j
public class DbUserStorage extends DbRequestableStorage<User> {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	@Autowired
	public DbUserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<User> getAll() {
		return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
	}

	@Override
	public User getById(Integer id) {
		return jdbcTemplate.query("SELECT * FROM users WHERE id_user=?", new UserMapper(), id).stream()
				.findAny().orElse(null);
	}

	@Override
	public User update(User user) {
		log.debug(String.valueOf(user.getId()));
		jdbcTemplate.update(
				"UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id_user=?",
				user.getLogin(), user.getName(),user.getEmail(), user.getBirthday(), user.getId());
		jdbcTemplate.query("SELECT * FROM users WHERE id_user=?", new UserMapper(), user.getId());
		return user;
	}

	@Override
	public User add(User user) {
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName("users").usingGeneratedKeyColumns("id_user");
		Number idUser = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
		user.setId(idUser.intValue());
		return user;
	}
}