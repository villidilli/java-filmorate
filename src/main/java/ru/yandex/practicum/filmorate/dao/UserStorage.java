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
	private final SimpleJdbcInsert jdbcInsert;

	@Autowired
	public UserStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName(USERS_TABLE.query)
				.usingGeneratedKeyColumns(USER_ID.query);
	}

	public List<User> getAllUsers() { //ref
		log.debug("/getAll");
		return jdbcTemplate.query(USER_GET_ALL.query, new UserMapper());
	}

	public int addUserAndReturnId(User user) { //ref
		log.debug("/addUserAndReturnId");
		return jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user)).intValue();
	}

	public User getById(Integer userId) { //ref
		log.debug("/getById");
		return jdbcTemplate.query(USER_GET_BY_ID.query, new UserMapper(), userId).stream()
				.findAny()
				.orElse(null);
	}

	public void updateUser(User user) { //ref
		log.debug("/updateUser");
		log.debug("income user: " + user.toString());
		jdbcTemplate.update(USER_UPDATE.query,
							user.getLogin(), user.getName(),user.getEmail(), user.getBirthday(), user.getId());
	}

	public void addFriend(int userId, int friendId) { //ref
		log.debug("/addFriend");
		log.debug("income userId / friendId [" + userId + "/" + friendId + "]");
		jdbcTemplate.update(ADD_FRIEND.query, userId, friendId);
	}

	public List<Integer> getFriendsIdByUserId(Integer userId) {
		return jdbcTemplate.queryForList("SELECT id_friend FROM user_friend WHERE id_user= ?", Integer.class, userId);
	}

	public List<User> getFriendsAsUser(Integer userId) { //ref TODO соединить
		log.debug("/getFriendsAsUser");
		log.debug("income userid: " + userId);
		return jdbcTemplate.query(GET_FRIENDS_AS_USER.query, new UserMapper(), userId);
		"SELECT * FROM users WHERE id_user IN (SELECT id_friend FROM user_friend WHERE id_user= ?)"
	}

	public List<Friend> getFriendsAsFriend(Integer userId) { // TODO соединить
		log.debug("/getFriendsAsFriend");
		return jdbcTemplate.query(GET_FRIENDS_AS_ID.query, new FriendMapper(), userId);
		"SELECT id_friend FROM user_friend WHERE id_user=?"
	}

	public Boolean isMutualFriendship(Integer userId, Integer checkedUserId) {
		List<Integer> userMutualFriends =
				jdbcTemplate.queryForList(COMMON_FRIENDS_USERS.query, Integer.class,  userId, userId);
		return userMutualFriends.contains(checkedUserId);
	}

	public void deleteFriend(Integer id, Integer friendId) {
		log.debug("/deleteFriend");
		jdbcTemplate.update(DELETE_FRIEND.query, id, friendId);
	}
}