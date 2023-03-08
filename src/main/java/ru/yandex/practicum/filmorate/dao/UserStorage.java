package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.List;

@Repository
@Slf4j
public class UserStorage {
	private final JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private final String tableName = "users";
	private final String nameIdColumn = "id_user";
	private final String queryGetAll = "SELECT * FROM " + tableName;
	private final String queryGetById  = "SELECT * FROM " + tableName + " WHERE " + nameIdColumn + "=?";
	private final String queryUpdate =
			"UPDATE " + tableName + " SET login=?, name=?, email=?, birthday=?" + "WHERE " + nameIdColumn + "=?";
	private final String queryGetObjById = "SELECT * FROM " + tableName + " WHERE " + nameIdColumn + "=?";
	private final String queryAddFriend = "INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)";
	private final String queryGetFriends =
			"SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user = ?)";

	@Autowired
	public UserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User add(User user) {
		log.debug("/add");
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName(tableName).usingGeneratedKeyColumns(nameIdColumn);
		Number idUser = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
		user.setId(idUser.intValue());
		return user;
	}

	public List<User> getAll() {
		log.debug("/getAll");
		return jdbcTemplate.query(queryGetAll, new UserMapper());
	}

	public User getById(Integer id) {
		log.debug("/getById");
		return jdbcTemplate.query(queryGetById, new UserMapper(), id).stream()
				.findAny().orElse(null);
	}

	public User update(User user) {
		log.debug("/update");
		log.debug(String.valueOf(user.getId()));
		jdbcTemplate.update(queryUpdate,
							user.getLogin(), user.getName(),user.getEmail(), user.getBirthday(), user.getId());
		jdbcTemplate.query(queryGetObjById, new UserMapper(), user.getId());
		return user;
	}

	public void addFriend(int id, int friendId) {
		log.debug("/addFriend");
		jdbcTemplate.update(queryAddFriend, id, friendId);
	}

	public List<User> getFriends(Integer id) {
		log.debug("/getFriends");
		return jdbcTemplate.query(queryGetFriends, new UserMapper(), id);
	}
}