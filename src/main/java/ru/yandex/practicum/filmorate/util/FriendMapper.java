//package ru.yandex.practicum.filmorate.util;
//
//import org.springframework.jdbc.core.RowMapper;
//import ru.yandex.practicum.filmorate.model.Friend;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class FriendMapper implements RowMapper<Friend> {
//	@Override
//	public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
//		Friend friend = new Friend();
//		friend.setId(rs.getInt("id_friend"));
//		return friend;
//	}
//}