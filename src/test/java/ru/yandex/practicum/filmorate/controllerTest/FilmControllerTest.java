package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.controller.ExceptionResponse;

import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.net.URI;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class FilmControllerTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    Film film1;
    Film film2;
    Film film3;
    String url = "/films";
    User user1;
    User user2;
    ResponseEntity<ExceptionResponse> entityExceptionResponse;
    ExceptionResponse exceptionResponse;

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute("ALTER TABLE films ALTER COLUMN id_film RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id_user RESTART WITH 1");

        film1 = new Film();
        film1.setName("Титаник");
        film1.setDescription("История о пароходе");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(140L);
        film1.getMpa().setId(1);
        Genre genre1 = new Genre();
        genre1.setId(1);
        Genre genre2 = new Genre();
        genre2.setId(2);
        film1.setGenres(List.of(genre1, genre2));

        film2 = new Film();
        film2.setName("О любви");
        film2.setDescription("История о любви");
        film2.setReleaseDate(LocalDate.of(2014, 6, 15));
        film2.setDuration(90L);
        film2.getMpa().setId(2);
        Genre genre3 = new Genre();
        genre3.setId(3);
        Genre genre4 = new Genre();
        genre4.setId(4);
        film2.setGenres(List.of(genre3, genre4));

        film3 = new Film();
        film3.setName("О войне");
        film3.setDescription("История о войне");
        film3.setReleaseDate(LocalDate.of(1980, 12, 31));
        film3.setDuration(220L);
        film3.getMpa().setId(3);
        Genre genre5 = new Genre();
        genre5.setId(5);
        Genre genre6 = new Genre();
        genre6.setId(6);
        film3.setGenres(List.of(genre5, genre6));

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
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute(
                "DELETE FROM films; DELETE FROM film_genre; DELETE FROM film_like; DELETE FROM users;");
    }

    @Test
    public void createFilmReturn201() {
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film1, Film.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.hasBody());
        film1.setId(1);
        assertEquals(film1, response.getBody());
        assertEquals(film1.getName(), response.getBody().getName());
    }

    @Test
    public void createFilmWhenReleaseDateInvalidReturn400() {
        film1.setReleaseDate(LocalDate.of(1500, 1, 1));
        entityExceptionResponse = restTemplate.postForEntity(url, film1, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
        exceptionResponse = entityExceptionResponse.getBody();
        assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
    }

    @Test
    public void updateFilmWhenReleaseDateInvalidReturn400() {
        film1.setReleaseDate(LocalDate.of(1500, 1, 1));
        entityExceptionResponse = restTemplate.exchange(
                new RequestEntity<>(film1, HttpMethod.PUT, URI.create(url)), ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
        exceptionResponse = entityExceptionResponse.getBody();
        assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
        assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
    }

    @Test
    public void getAllFilmsReturn200() {
        restTemplate.postForEntity("/films", film1, Film.class);
        restTemplate.postForEntity("/films", film2, Film.class);
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films", Film[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(2, response.getBody().length);
        Film actFilm1 = response.getBody()[0];
        Film actFilm2 = response.getBody()[1];
        assertEquals(film1.getName(), actFilm1.getName());
        assertEquals(film1.getMpa().getId(), actFilm1.getMpa().getId());
        assertNotNull(actFilm1.getMpa().getName());
        assertArrayEquals(film1.getGenres().toArray(), actFilm1.getGenres().toArray());
        assertEquals(film2.getName(), actFilm2.getName());
        assertEquals(film2.getMpa().getId(), actFilm2.getMpa().getId());
        assertNotNull(actFilm2.getMpa().getName());
        assertArrayEquals(film2.getGenres().toArray(), actFilm2.getGenres().toArray());
    }

    @Test
    public void getFilmById1Return200() {
        restTemplate.postForEntity("/films", film1, Film.class);
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        Film actFilm1 = response.getBody();
        assertEquals(film1.getName(), actFilm1.getName());
        assertEquals(film1.getMpa().getId(), actFilm1.getMpa().getId());
        assertNotNull(actFilm1.getMpa().getName());
        assertArrayEquals(film1.getGenres().toArray(), actFilm1.getGenres().toArray());
    }

    @Test
    public void getFilmById999Return404() {
        ResponseEntity<ExceptionResponse> response =
                restTemplate.getForEntity("/films/999", ExceptionResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        ExceptionResponse ex = response.getBody();
        assertTrue(ex.getExceptionClass().contains("NotFoundException"));
        assertTrue(ex.getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
    }

    @Test
    public void addLikeFilm1User1Return204() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/films", film1, Film.class);
        ResponseEntity response = restTemplate.exchange(
                new RequestEntity<>(HttpMethod.PUT, URI.create("/films/1/like/1")), ResponseEntity.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        Map<String, Object> responce1 = jdbcTemplate.queryForMap("SELECT * FROM film_like");
        assertFalse(responce1.isEmpty());
        assertEquals(1, responce1.get("id_film"));
        assertEquals(1, responce1.get("id_user"));
    }

    @Test
    public void addLikeFilm999User1Return404() {
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                new RequestEntity<>(HttpMethod.PUT, URI.create("/films/999/like/1")), ExceptionResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("NotFoundException", response.getBody().getExceptionClass());
        assertTrue(response.getBody().getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
    }

    @Test
    public void deleteLikeFilm1User1Return204WhenTableEmpty() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/films", film1, Film.class);
        restTemplate.exchange(
                new RequestEntity<>(HttpMethod.PUT, URI.create("/films/1/like/1")), ResponseEntity.class);
        ResponseEntity response = restTemplate.exchange(
                new RequestEntity<>(HttpMethod.DELETE, URI.create("/films/1/like/1")), ResponseEntity.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        EmptyResultDataAccessException ex = assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForMap("SELECT * FROM film_like"));
        assertEquals("EmptyResultDataAccessException", ex.getClass().getSimpleName());
        assertTrue(ex.getMessage().contains("Incorrect result size"));
    }

    @Test
    public void deleteLikeFilm999User1Return404() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/films", film1, Film.class);
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                new RequestEntity<>(HttpMethod.PUT, URI.create("/films/999/like/1")), ExceptionResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("NotFoundException", response.getBody().getExceptionClass());
        assertTrue(response.getBody().getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
    }

    @Test
    public void getAllMpaReturn200() {
        ResponseEntity<Mpa[]> response = restTemplate.getForEntity("/mpa", Mpa[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(5, response.getBody().length);
        assertEquals("G", response.getBody()[0].getName());
        assertEquals("PG", response.getBody()[1].getName());
        assertEquals("PG-13", response.getBody()[2].getName());
        assertEquals("R", response.getBody()[3].getName());
        assertEquals("NC-17", response.getBody()[4].getName());
    }

    @Test
    public void getMpaById1Return200() {
        ResponseEntity<Mpa> response = restTemplate.getForEntity("/mpa/1", Mpa.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        Mpa actMpa1 = response.getBody();
        assertEquals("G", actMpa1.getName());
    }

    @Test
    public void getMpaById999Return404() {
        ResponseEntity<ExceptionResponse> response =
                restTemplate.getForEntity("/mpa/999", ExceptionResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        ExceptionResponse ex = response.getBody();
        assertTrue(ex.getExceptionClass().contains("NotFoundException"));
        assertTrue(ex.getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
    }

    @Test
    public void getAllGenresReturn200() {
        ResponseEntity<Genre[]> response = restTemplate.getForEntity("/genres", Genre[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(6, response.getBody().length);
        assertEquals("Комедия", response.getBody()[0].getName());
        assertEquals("Драма", response.getBody()[1].getName());
        assertEquals("Мультфильм", response.getBody()[2].getName());
        assertEquals("Триллер", response.getBody()[3].getName());
        assertEquals("Документальный", response.getBody()[4].getName());
        assertEquals("Боевик", response.getBody()[5].getName());
    }

    @Test
    public void getGenreId3Return200() {
        ResponseEntity<Genre> response = restTemplate.getForEntity("/genres/3", Genre.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Мультфильм", response.getBody().getName());
    }

    @Test
    public void getGenreId999Return404() {
        ResponseEntity<ExceptionResponse> response =
                restTemplate.getForEntity("/genres/999", ExceptionResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("NotFoundException", response.getBody().getExceptionClass());
        assertTrue(response.getBody().getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
    }

    @Test
    public void getPopularFilmsReturn200() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/users", user2, User.class);
        restTemplate.postForEntity("/films", film1, Film.class);
        restTemplate.postForEntity("/films", film2, Film.class);
        restTemplate.postForEntity("/films", film3, Film.class);
        restTemplate.exchange(new RequestEntity<>(HttpMethod.PUT, URI.create("/films/2/like/1")), ResponseEntity.class);
        restTemplate.exchange(new RequestEntity<>(HttpMethod.PUT, URI.create("/films/3/like/1")), ResponseEntity.class);
        restTemplate.exchange(new RequestEntity<>(HttpMethod.PUT, URI.create("/films/3/like/2")), ResponseEntity.class);
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films/popular", Film[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(3, response.getBody().length);
        assertTrue(response.getBody()[0].getRate() >= 0);
        assertTrue(response.getBody()[1].getRate() >= 0);
        assertTrue(response.getBody()[2].getRate() >= 0);
        assertEquals(film3.getName(), response.getBody()[0].getName());
        assertEquals(film2.getName(), response.getBody()[1].getName());
        assertEquals(film1.getName(), response.getBody()[2].getName());
    }

    @Test
    public void getPopularFilmsReturn200WhenEmpty() {
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films/popular", Film[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(0, response.getBody().length);
    }
}