package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class UserControllerTest {
	UserController controller;
	private ResponseEntity<User> response;
	private User user1;
	private User actualUser;

	@BeforeEach
	public void beforeEach() {
		System.err.close();
		System.setErr(System.out);
		controller = new UserController();
		user1 = new User();
		user1.setName("Ваня");
		user1.setLogin("vanya");
		user1.setEmail("vanya@mail.ru");
		user1.setBirthday(LocalDate.of(1990, 6, 15));
	}

	@Test
	public void shouldReturnStatus400AndBodyWhenLoginHaveSpaceCreateMethod() {
		user1.setLogin("log in");
		response = controller.create(user1);
		actualUser = response.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(actualUser.getId());
		assertEquals(user1.getName(), actualUser.getName());
		assertEquals(user1.getEmail(), actualUser.getEmail());
		assertEquals(user1.getBirthday(), actualUser.getBirthday());
		assertEquals(user1.getLogin(), actualUser.getLogin());
	}

	@Test
	public void shouldReturnStatus200AndNameEqualsLoginWhenNameIsBlankCreateMethod() {
		user1.setName(null);
		response = controller.create(user1);
		actualUser = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user1.getLogin(), actualUser.getName());
		assertEquals(1, actualUser.getId());
		assertEquals(user1.getName(), actualUser.getName());
		assertEquals(user1.getEmail(), actualUser.getEmail());
		assertEquals(user1.getBirthday(), actualUser.getBirthday());
		assertEquals(user1.getLogin(), actualUser.getLogin());
	}

	@Test
	public void shouldReturnStatus400AndBodyWhenLoginHaveSpaceUpdateMethod() {
		controller.create(user1);
		user1.setId(1);
		user1.setLogin("log in");
		response = controller.update(user1);
		actualUser = response.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(1, actualUser.getId());
		assertEquals(user1.getName(), actualUser.getName());
		assertEquals(user1.getEmail(), actualUser.getEmail());
		assertEquals(user1.getBirthday(), actualUser.getBirthday());
		assertEquals(user1.getLogin(), actualUser.getLogin());
	}

	@Test
	public void shouldReturnStatus200AndNameEqualsLoginWhenNameIsBlankUpdateMethod() {
		controller.create(user1);
		user1.setId(1);
		user1.setName(null);
		response = controller.update(user1);
		actualUser = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user1.getLogin(), actualUser.getName());
		assertEquals(1, actualUser.getId());
		assertEquals(user1.getName(), actualUser.getName());
		assertEquals(user1.getEmail(), actualUser.getEmail());
		assertEquals(user1.getBirthday(), actualUser.getBirthday());
		assertEquals(user1.getLogin(), actualUser.getLogin());
	}
}