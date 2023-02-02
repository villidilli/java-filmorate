package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;

import ru.yandex.practicum.filmorate.controller.ExceptionResponse;

import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.User;

import java.net.URI;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	@Autowired
	TestRestTemplate restTemplate;
	User user;
	String url = "/users";
	User actualUser;
	ResponseEntity<User> entityUser;
	ResponseEntity<ExceptionResponse> entityExceptionResponse;
	ExceptionResponse exceptionResponse;

	@BeforeEach
	public void beforeEach() {
		user = new User();
		user.setLogin("Vasssssssya");
		user.setEmail("vasya@ya.ru");
		user.setName("Vasya");
		user.setBirthday(LocalDate.of(1995, 1, 1));
	}

	@Test
	public void shouldReturnStatus400IfLoginWithSpaceWhenPOST() {
		user.setLogin("invalid login");
		entityExceptionResponse = restTemplate.postForEntity(url, user, ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
	}

	@Test
	public void shouldReturnStatus200AndNameEqualsLoginWhenPOST() {
		user.setName(null);
		entityUser = restTemplate.postForEntity(url, user, User.class);
		assertEquals(HttpStatus.OK, entityUser.getStatusCode());
		actualUser = entityUser.getBody();
		assertEquals(actualUser.getName(), actualUser.getLogin());
	}

	@Test
	public void shouldReturnStatus400IfLoginWithSpaceWhenPUT() {
		user.setLogin("invalid login");
		entityExceptionResponse = restTemplate.exchange(
								new RequestEntity<>(user, HttpMethod.PUT, URI.create(url)), ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
	}

	@Test
	public void shouldReturnStatus200AndNameEqualsLoginWhenPUT() {
		entityUser = restTemplate.postForEntity(url, user, User.class);
		assertEquals(HttpStatus.OK, entityUser.getStatusCode());
		int id = entityUser.getBody().getId();
		user.setId(id);
		user.setName(null);
		entityUser = restTemplate.exchange(
				new RequestEntity<>(user, HttpMethod.PUT, URI.create(url)), User.class);
		assertEquals(HttpStatus.OK, entityUser.getStatusCode());
		actualUser = entityUser.getBody();
		assertEquals(id, actualUser.getId());
		assertEquals(actualUser.getName(), actualUser.getLogin());
	}
}