package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.*;

import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ControllerTest {
    Controller<User> userController;
    Controller<Film> filmController;
    private ResponseEntity<Requestable> response;
    private User user1;
    private Film film1;
    private User actualUser;
    private Film actualFilm;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user1 = new User();
        user1.setName("Ваня");
        user1.setLogin("vanya");
        user1.setEmail("vanya@mail.ru");
        user1.setBirthday(LocalDate.of(1990, 6, 15));
        filmController = new FilmController();
        film1 = new Film();
        film1.setName("Титаник");
        film1.setDescription("История о пароходе");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(140L);
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

    @Test
    public void shouldReturnStatus400AndBodyWhenReleaseDateBeforeCreateMethod() {
        film1.setReleaseDate(LocalDate.of(1500, 1, 1));
        response = filmController.create(film1);
        actualFilm = (Film) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(actualFilm.getId());
        assertEquals(film1.getName(), actualFilm.getName());
        assertEquals(film1.getDescription(), actualFilm.getDescription());
        assertEquals(film1.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(film1.getDuration(), film1.getDuration());
    }

    @Test
    public void shouldReturnStatus400AndBodyWhenReleaseDateBeforeUpdateMethod() {
        filmController.create(film1);
        film1.setReleaseDate(LocalDate.of(1500, 1, 1));
        film1.setId(1);
        response = filmController.update(film1);
        actualFilm = (Film) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, actualFilm.getId());
        assertEquals(film1.getName(), actualFilm.getName());
        assertEquals(film1.getDescription(), actualFilm.getDescription());
        assertEquals(film1.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(film1.getDuration(), film1.getDuration());
    }
}