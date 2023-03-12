package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.ExceptionResponse;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.User;

import java.net.URI;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UserControllerTest {
	@Autowired
	TestRestTemplate restTemplate;
	@Autowired
	JdbcTemplate jdbcTemplate;
	User user1;
	User user2;
	User user3;
	String url = "/users";
	User actualUser;
	ResponseEntity<User> entityUser1;
	ResponseEntity<User> entityUser2;

	ResponseEntity<User[]> users;
	ResponseEntity<ExceptionResponse> entityExceptionResponse;
	ExceptionResponse exceptionResponse;


	@BeforeEach
	public void beforeEach() {
		jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id_user RESTART WITH 1");
		user1 = new User();
		user1.setLogin("Vasssssssya");
		user1.setEmail("vasya@ya.ru");
		user1.setName("Vasya");
		user1.setBirthday(LocalDate.of(1995, 1, 1));

		user2 = new User();
		user2.setLogin("Fedddddddya");
		user2.setEmail("fedya@ya.ru");
		user2.setName("Fedya");
		user2.setBirthday(LocalDate.of(1990, 12, 31));

		user3 = new User();
		user3.setLogin("Mashhhhhhhha");
		user3.setEmail("masha@ya.ru");
		user3.setName("Masha");
		user3.setBirthday(LocalDate.of(2000, 6, 15));
	}

	@AfterEach
	public void afterEach() {
		jdbcTemplate.execute("DELETE FROM users; DELETE FROM user_friend;");
	}

	@Test
	public void createUser() {
		ResponseEntity<User> response = restTemplate.postForEntity("/users", user1, User.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.hasBody());
		user1.setId(1);
		assertEquals(user1, response.getBody());
	}

	@Test
	public void createUserWhenLoginWithSpace() {
		user1.setLogin("invalid login");
		entityExceptionResponse = restTemplate.postForEntity("/users", user1, ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
	}

	@Test
	public void createUserWhenNameIsBlank() {
		user1.setName(null);
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		assertEquals(HttpStatus.CREATED, entityUser1.getStatusCode());
		actualUser = entityUser1.getBody();
		assertEquals(actualUser.getName(), actualUser.getLogin());
	}

	@Test
	public void getAllUsers() {
		restTemplate.postForEntity("/users", user1, User.class);
		restTemplate.postForEntity("/users", user2, User.class);
		restTemplate.postForEntity("/users", user3, User.class);
		ResponseEntity<User[]> users = restTemplate.getForEntity("/users", User[].class);
		user1.setId(1);
		user2.setId(2);
		user3.setId(3);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertNotNull(users.getBody());
		assertEquals(user1, users.getBody()[0]);
		assertEquals(user2, users.getBody()[1]);
		assertEquals(user3, users.getBody()[2]);
	}

	@Test
	public void getCommonFriendsUserId1And2() {
		restTemplate.postForEntity("/users", user1, User.class);
		restTemplate.postForEntity("/users", user2, User.class);
		restTemplate.postForEntity("/users", user3, User.class);
		restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/1/friends/3")), ResponseEntity.class);
		restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/2/friends/3")), ResponseEntity.class);
		ResponseEntity<User[]> users = restTemplate.getForEntity(
				"/users/1/friends/common/2", User[].class);
		user3.setId(3);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertNotNull(users.getBody());
		assertEquals(user3, users.getBody()[0]);
	}

	@Test
	public void getFriendsUserId1FriendId2() {
		restTemplate.postForEntity("/users", user1, User.class);
		restTemplate.postForEntity("/users", user2, User.class);
		restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/1/friends/2")), ResponseEntity.class);
		ResponseEntity<User[]> users = restTemplate.getForEntity(
				"/users/1/friends", User[].class);
		user2.setId(2);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertNotNull(users.getBody());
		assertEquals(1, users.getBody().length);
		assertEquals(user2, users.getBody()[0]);
	}

	@Test
	public void getFriendsUserId1WhenNotFriend() {
		restTemplate.postForEntity("/users", user1, User.class);
		ResponseEntity<User[]> users = restTemplate.getForEntity(
				"/users/1/friends", User[].class);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertNotNull(users.getBody());
		assertEquals(0, users.getBody().length);
	}

	@Test
	public void getFriendsUserId1WhenFriend999NotFound() {
		ResponseEntity<Map> friends = restTemplate.getForEntity(
				"/users/999/friends", Map.class);
		assertEquals(HttpStatus.NOT_FOUND, friends.getStatusCode());
		assertNotNull(friends.getBody());
		assertEquals("NotFoundException", friends.getBody().get("exceptionClass"));
		assertEquals("[id: 999][Объект по ID не найден]", friends.getBody().get("exceptionMessage"));
	}

	@Test
	public void getCommonFriendsUserId1And2WhenHaveNotFriends() {
		restTemplate.postForEntity("/users", user1, User.class);
		restTemplate.postForEntity("/users", user2, User.class);
		restTemplate.postForEntity("/users", user3, User.class);
		restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/1/friends/2")), ResponseEntity.class);
		restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/2/friends/3")), ResponseEntity.class);
		ResponseEntity<User[]> users = restTemplate.getForEntity(
				"/users/1/friends/common/2", User[].class);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertNotNull(users.getBody());
		assertEquals(0, users.getBody().length);
	}

	@Test
	public void getUserId1WhenUserCreated() {
		restTemplate.postForEntity("/users", user1, User.class);
		ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
		user1.setId(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user1.getId(), response.getBody().getId());
		assertEquals(user1.getName(), response.getBody().getName());
	}

	@Test
	public void getUserId1WhenUserNotCreated() {
		ResponseEntity<Map> response = restTemplate.exchange(
				new RequestEntity<>(HttpMethod.GET, URI.create("/users/1")), Map.class);
		assertTrue(response.hasBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("NotFoundException", response.getBody().get("exceptionClass"));
		assertEquals("[id: 1][Объект по ID не найден]", response.getBody().get("exceptionMessage"));
	}

	@Test
	public void getAllUsersWhenUsersNotHave() {
		users = restTemplate.getForEntity("/users", User[].class);
		assertEquals(HttpStatus.OK, users.getStatusCode());
		assertEquals(0, users.getBody().length);
	}

	@Test
	public void addFriendWhenFriendNotNull() {
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		entityUser2 = restTemplate.postForEntity("/users", user2, User.class);
		ResponseEntity response = restTemplate.exchange(
							new RequestEntity<>(HttpMethod.PUT, URI.create("/users/1/friends/2")),
								ResponseEntity.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertFalse(response.hasBody());
	}

	@Test
	public void deleteFriendWhenFriendNotNull() {
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		entityUser2 = restTemplate.postForEntity("/users", user2, User.class);
		ResponseEntity response = restTemplate.exchange(
				new RequestEntity<>(HttpMethod.DELETE, URI.create("/users/1/friends/2")),
				ResponseEntity.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertFalse(response.hasBody());
	}

	@Test
	public void addFriendWhenFriendNull() {
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		ResponseEntity<Map> response = restTemplate.exchange(
				new RequestEntity<>(HttpMethod.PUT, URI.create("/users/1/friends/2")), Map.class);
		assertTrue(response.hasBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("NotFoundException", response.getBody().get("exceptionClass"));
		assertEquals("[id: 2][Объект по ID не найден]", response.getBody().get("exceptionMessage"));
	}

	@Test
	public void deleteFriendWhenFriendNull() {
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		ResponseEntity<Map> response = restTemplate.exchange(
				new RequestEntity<>(HttpMethod.DELETE, URI.create("/users/1/friends/2")), Map.class);
		assertTrue(response.hasBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("NotFoundException", response.getBody().get("exceptionClass"));
		assertEquals("[id: 2][Объект по ID не найден]", response.getBody().get("exceptionMessage"));
	}

	@Test
	public void userUpdateWhenLoginWithSpace() {
		user1.setLogin("invalid login");
		entityExceptionResponse = restTemplate.exchange(
							new RequestEntity<>(user1, HttpMethod.PUT, URI.create("/users")), ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
	}

	@Test
	public void userUpdateWhenNameIsBlank() {
		entityUser1 = restTemplate.postForEntity("/users", user1, User.class);
		assertEquals(HttpStatus.CREATED, entityUser1.getStatusCode());
		int id = entityUser1.getBody().getId();
		user1.setId(id);
		user1.setName(null);
		entityUser1 = restTemplate.exchange(
				new RequestEntity<>(user1, HttpMethod.PUT, URI.create(url)), User.class);
		assertEquals(HttpStatus.OK, entityUser1.getStatusCode());
		actualUser = entityUser1.getBody();
		assertEquals(id, actualUser.getId());
		assertEquals(actualUser.getName(), actualUser.getLogin());
	}
}