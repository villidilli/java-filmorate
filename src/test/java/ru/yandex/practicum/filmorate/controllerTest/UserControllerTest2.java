package ru.yandex.practicum.filmorate.controllerTest;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.ExceptionResponse;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest2 {
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

    @BeforeEach
    public void beforeEach() {
        userController.getObjects().clear();
        user = new User();
        user.setLogin("Vasssssssya");
        user.setEmail("vasya@ya.ru");
        user.setName("Vasya");
        user.setBirthday(LocalDate.of(1995, 1, 1));
    }

    @Test
    public void shouldReturn200AndUserWithIdNotNull() {
        entityUser = restTemplate.postForEntity(url, user, User.class);
        assertTrue(entityUser.hasBody());
        assertEquals(200, entityUser.getStatusCodeValue());
        actualUser = entityUser.getBody();
        System.out.println(restTemplate.getForEntity(url, List.class));
        assertNotNull(actualUser.getId());
        assertTrue(actualUser.getId() > 0);
        assertEquals(user.getLogin(), actualUser.getLogin());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getBirthday(), actualUser.getBirthday());
    }

    @Test
    public void shouldReturn400AndValidateExceptionByLogin() {
        user.setLogin("invalid login");
        entityExceptionResponse = restTemplate.postForEntity(url, user, ExceptionResponse.class);
        exceptionResponse = entityExceptionResponse.getBody();
        assertTrue(entityExceptionResponse.hasBody());
        assertEquals(400, entityExceptionResponse.getStatusCodeValue());
        assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("Login"));
    }

    @Test
    public void shouldReturn400AndValidateExceptionByEmail() {
        user.setEmail("invalidEmail");
        entityExceptionResponse = restTemplate.postForEntity(url, user, ExceptionResponse.class);
        exceptionResponse = entityExceptionResponse.getBody();
        assertTrue(entityExceptionResponse.hasBody());
        assertEquals(400, entityExceptionResponse.getStatusCodeValue());
        assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("email"));
    }

    @Test
    public void shouldReturn400AndValidateExceptionByBirthday() {
        user.setBirthday(LocalDate.of(3000,1,1));
        entityExceptionResponse = restTemplate.postForEntity(url, user, ExceptionResponse.class);
        exceptionResponse = entityExceptionResponse.getBody();
        assertTrue(entityExceptionResponse.hasBody());
        assertEquals(400, entityExceptionResponse.getStatusCodeValue());
        assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("birthday"));
    }

    @Test
    public void shouldReturn200AndUserWithUpdateFieldWithSameId() {
        int id = restTemplate.postForObject(url, user, User.class).getId();
        user.setId(id);
        user.setLogin("newLogin");
        entityUser = restTemplate.exchange(new RequestEntity<>(user, HttpMethod.PUT, URI.create(url)), User.class);
        assertTrue(entityUser.hasBody());
        assertEquals(200, entityUser.getStatusCodeValue());
        actualUser = entityUser.getBody();
        assertEquals(id, actualUser.getId());
        assertEquals("newLogin", actualUser.getLogin());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getBirthday(), actualUser.getBirthday());
    }

    @Test
    public void shouldReturn404AndNotFoundException() {
        user.setId(9999);
        entityExceptionResponse = restTemplate.exchange(
                new RequestEntity<>(user, HttpMethod.PUT, URI.create(url)),
                ExceptionResponse.class);
        assertTrue(entityExceptionResponse.hasBody());
        exceptionResponse = entityExceptionResponse.getBody();
        assertEquals(404, entityExceptionResponse.getStatusCodeValue());
        assertEquals(NotFoundException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("id: 9999"));
    }

    @Test
    public void shouldReturnAddedUsers() {
        User user1 = restTemplate.postForObject(url, user, User.class);
        User user2 = restTemplate.postForObject(url, user, User.class);
        User[] expectedArray = new User[] {user1, user2};
        Type type = new TypeToken<User[]>() {}.getClass();
        User[] actualArray = restTemplate.getForObject(url, User[].class);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void shouldReturnNameEqualsLoginWhenNameIsBlank() {
        user.setName(null);
        actualUser = restTemplate.postForObject(url, user, User.class);
        assertEquals(user.getLogin(), actualUser.getName());
    }


}