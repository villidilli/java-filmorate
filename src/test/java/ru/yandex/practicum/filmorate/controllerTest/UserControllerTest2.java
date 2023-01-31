package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.ExceptionResponse;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest2 {
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
    public void shouldReturn200AndUserWithId1() {
        entityUser = restTemplate.postForEntity(url, user, User.class);
        assertTrue(entityUser.hasBody());
        assertEquals(200, entityUser.getStatusCodeValue());
        actualUser = entityUser.getBody();
        assertEquals(1, actualUser.getId());
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
    public void shouldReturn200AndUserWithUpdateFieldWithId1() {
        user.setId(1);
        user.setLogin("newLogin");
        entityUser = restTemplate.exchange(new RequestEntity<>(user, HttpMethod.PUT, URI.create("http://localhost:8080/users")), User.class);
        assertTrue(entityUser.hasBody());
        assertEquals(200, entityUser.getStatusCodeValue());
        actualUser = entityUser.getBody();
        assertEquals(1, actualUser.getId());
        assertEquals("newLogin", actualUser.getLogin());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getBirthday(), actualUser.getBirthday());
    }
}
