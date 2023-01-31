package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.UserController;

import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserControllerTest {
    UserController userController;
    private ResponseEntity<Requestable> response;
    private User user1;
    private User actualUser;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user1 = new User();
        user1.setName("Ваня");
        user1.setLogin("vanya");
        user1.setEmail("vanya@mail.ru");
        user1.setBirthday(LocalDate.of(1990, 6, 15));
    }

    @Test
    public void shouldReturnStatus400AndBodyWhenLoginHaveSpaceCreateMethod() {
        user1.setLogin("log in");
        response = userController.create(user1);
        actualUser = (User) response.getBody();
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
        response = userController.create(user1);
        actualUser = (User) response.getBody();
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
        userController.create(user1);
        user1.setId(1);
        user1.setLogin("log in");
        response = userController.update(user1);
        actualUser = (User) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, actualUser.getId());
        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
        assertEquals(user1.getBirthday(), actualUser.getBirthday());
        assertEquals(user1.getLogin(), actualUser.getLogin());
    }

    @Test
    public void shouldReturnStatus200AndNameEqualsLoginWhenNameIsBlankUpdateMethod() {
        userController.create(user1);
        user1.setId(1);
        user1.setName(null);
        response = userController.update(user1);
        actualUser = (User) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user1.getLogin(), actualUser.getName());
        assertEquals(1, actualUser.getId());
        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
        assertEquals(user1.getBirthday(), actualUser.getBirthday());
        assertEquals(user1.getLogin(), actualUser.getLogin());
    }
}