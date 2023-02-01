package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.ExceptionResponse;
import ru.yandex.practicum.filmorate.controller.UserController;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	@Autowired
	TestRestTemplate restTemplate;
	@Autowired
	UserController userController;
	User user;
	String url = "/users";
	User actualUser;
	ResponseEntity<User> entityUser;
	ResponseEntity<ExceptionResponse> entityExceptionResponse;
	ExceptionResponse exceptionResponse;

//	@BeforeEach
//	public void beforeEach() {
//		userController.getObjects().clear();
//		user = new User();
//		user.setLogin("Vasssssssya");
//		user.setEmail("vasya@ya.ru");
//		user.setName("Vasya");
//		user.setBirthday(LocalDate.of(1995, 1, 1));
//	}
//
//    @Test
//    public void shouldReturnStatus400AndBodyWhenLoginHaveSpaceCreateMethod() {
//		user.setLogin("invalid login");
//		entityExceptionResponse = restTemplate.postForEntity(url, user, ExceptionResponse.class);
//		exceptionResponse = entityExceptionResponse.getBody();
//		assertTrue(entityExceptionResponse.hasBody());
//		assertEquals(400, entityExceptionResponse.getStatusCodeValue());
//		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
//		assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
//    }
//
//    @Test
//    public void shouldReturnStatus200AndNameEqualsLoginWhenNameIsBlankCreateMethod() {
//        user.setName(null);
//        response = userController.create(user1);
//        actualUser = restTemplate
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user1.getLogin(), actualUser.getName());
//        assertEquals(1, actualUser.getId());
//        assertEquals(user1.getName(), actualUser.getName());
//        assertEquals(user1.getEmail(), actualUser.getEmail());
//        assertEquals(user1.getBirthday(), actualUser.getBirthday());
//        assertEquals(user1.getLogin(), actualUser.getLogin());
//    }
//
//    @Test
//    public void shouldReturnStatus400AndBodyWhenLoginHaveSpaceUpdateMethod() {
//        userController.create(user1);
//        user1.setId(1);
//        user1.setLogin("log in");
//        response = userController.update(user1);
//        actualUser = (User) response.getBody();
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(1, actualUser.getId());
//        assertEquals(user1.getName(), actualUser.getName());
//        assertEquals(user1.getEmail(), actualUser.getEmail());
//        assertEquals(user1.getBirthday(), actualUser.getBirthday());
//        assertEquals(user1.getLogin(), actualUser.getLogin());
//    }
//
//    @Test
//    public void shouldReturnStatus200AndNameEqualsLoginWhenNameIsBlankUpdateMethod() {
//        userController.create(user1);
//        user1.setId(1);
//        user1.setName(null);
//        response = userController.update(user1);
//        actualUser = (User) response.getBody();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user1.getLogin(), actualUser.getName());
//        assertEquals(1, actualUser.getId());
//        assertEquals(user1.getName(), actualUser.getName());
//        assertEquals(user1.getEmail(), actualUser.getEmail());
//        assertEquals(user1.getBirthday(), actualUser.getBirthday());
//        assertEquals(user1.getLogin(), actualUser.getLogin());
//    }
}