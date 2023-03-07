package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.List;

@Repository
@Slf4j
public class UserStorage extends RequestableStorage<User> {
	protected JdbcTemplate jdbcTemplate;
	protected SimpleJdbcInsert jdbcInsert;
	protected String tableName;
	protected String nameIdColumn;
	protected String queryGetAll;
	protected String queryGetById;
	protected String queryUpdate;
	protected String queryGetObjById;
	protected String queryAddFriend;

	@Autowired
	public UserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.tableName = "users";
		this.nameIdColumn = "id_user";
		this.queryGetAll = "SELECT * FROM " + tableName;
		this.queryGetById = "SELECT * FROM " + tableName + " WHERE " + nameIdColumn + "=?";
		this.queryUpdate =
				"UPDATE " + tableName + " SET login=?, name=?, email=?, birthday=?" + "WHERE " + nameIdColumn + "=?";
		this.queryGetObjById = "SELECT * FROM " + tableName + " WHERE " + nameIdColumn + "=?";
		this.queryAddFriend = "INSERT INTO user_friend (id_user, id_friend) VALUES (?,?)";
	}

	@Override
	public User add(User user) {
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName(tableName).usingGeneratedKeyColumns(nameIdColumn);
		Number idUser = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
		user.setId(idUser.intValue());
		return user;
	}


	@Override
	public List<User> getAll() {
		log.debug("/getAll");
		return jdbcTemplate.query(queryGetAll, new UserMapper());
	}

	@Override
	public User getById(Integer id) {
		log.debug("/getById");
		return jdbcTemplate.query(queryGetById, new UserMapper(), id).stream()
				.findAny().orElse(null);
	}

	@Override
	public User update(User user) {
		log.debug("/update");
		log.debug(String.valueOf(user.getId()));
		jdbcTemplate.update(queryUpdate,
							user.getLogin(), user.getName(),user.getEmail(), user.getBirthday(), user.getId());
		jdbcTemplate.query(queryGetObjById, new UserMapper(), user.getId());
		return user;
	}

	@Override
	public void addFriend(int id, int friendId) {
		jdbcTemplate.update(queryAddFriend, id, friendId);
	}
}