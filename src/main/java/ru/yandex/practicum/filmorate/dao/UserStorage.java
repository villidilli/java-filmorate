package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.FriendMapper;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;

@Repository
@Slf4j
public class UserStorage {
	private final JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	@Autowired
	public UserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User add(User user) { //todo добавить запрос из БД
		log.debug("/add");
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName(USERS_TABLE.query).usingGeneratedKeyColumns(USER_ID.query);
		Number idUser = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
		return jdbcTemplate.queryForObject(USER_GET_BY_ID.query, new UserMapper(), idUser.intValue());
	}

	public List<User> getAll() {
		log.debug("/getAll");
		return jdbcTemplate.query(USER_GET_ALL.query, new UserMapper());
	}

	public User getById(Integer id) {
		log.debug("/getById");
		return jdbcTemplate.query(USER_GET_BY_ID.query, new UserMapper(), id).stream()
				.findAny().orElse(null);
//		return jdbcTemplate.queryForObject(USER_GET_BY_ID.query, new UserMapper(), id);
	}

	public User update(User user) { //todo добавить запрос из БД
		log.debug("/update");
		log.debug(String.valueOf(user.getId()));
		jdbcTemplate.update(USER_UPDATE.query,
							user.getLogin(), user.getName(),user.getEmail(), user.getBirthday(), user.getId());
		return jdbcTemplate.queryForObject(USER_GET_BY_ID.query, new UserMapper(), user.getId());
	}

	public void addFriend(int id, int friendId) {
		log.debug("/addFriend");
		jdbcTemplate.update(ADD_FRIEND.query, id, friendId);
	}

	public List<User> getFriendsAsUser(Integer id) {
		log.debug("/getFriendsAsUser");
		return jdbcTemplate.query(GET_FRIENDS_AS_USER.query, new UserMapper(), id);
	}

	public List<Friend> getFriendsAsFriend(Integer id) {
		log.debug("/getFriendsAsFriend");
		return jdbcTemplate.query(GET_FRIENDS_AS_ID.query, new FriendMapper(), id);
	}

	public Boolean getStatusFriendShip(Integer id, Integer id2) {
		List<Integer> commonFriends = jdbcTemplate.queryForList(COMMON_FRIENDS_USERS.query, Integer.class,  id, id);
		return commonFriends.contains(id2);
	}

	public void deleteFriend(Integer id, Integer friendId) {
		log.debug("/deleteFriend");
		jdbcTemplate.update(DELETE_FRIEND.query, id, friendId);
	}
}