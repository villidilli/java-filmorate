package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.core.ParameterizedTypeReference;
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
import ru.yandex.practicum.filmorate.util.FilmMapper;
import ru.yandex.practicum.filmorate.util.GenreMapper;
import ru.yandex.practicum.filmorate.util.MpaMapper;

import java.net.URI;

import java.time.LocalDate;
import java.util.List;

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
	Film actualFilm;
	ResponseEntity<Film> entityFilm;
	ResponseEntity<ExceptionResponse> entityExceptionResponse;
	ExceptionResponse exceptionResponse;
	ParameterizedTypeReference<List<Film>> listFilmType= new ParameterizedTypeReference<>() {};

	@BeforeEach
	public void beforeEach() {
		jdbcTemplate.execute("ALTER TABLE films ALTER COLUMN id_film RESTART WITH 1");

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
		film2.getMpa().setId(3);
		Genre genre5 = new Genre();
		genre5.setId(5);
		Genre genre6 = new Genre();
		genre6.setId(6);
		film3.setGenres(List.of(genre5, genre6));
	}

	@AfterEach
	public void afterEach() {
		jdbcTemplate.execute("DELETE FROM films; DELETE FROM film_genre; DELETE FROM film_like");
	}

	@Test
	public void createFilm() {
		ResponseEntity<Film> response = restTemplate.postForEntity("/films", film1, Film.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.hasBody());
		film1.setId(1);
		assertEquals(film1, response.getBody());
		assertEquals(film1.getName(), response.getBody().getName());
	}

	@Test
	public void createFilmWhenReleaseDateInvalid() {
		film1.setReleaseDate(LocalDate.of(1500, 1, 1));
		entityExceptionResponse = restTemplate.postForEntity(url, film1, ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
	}

	@Test
	public void updateFilmWhenReleaseDateInvalid() {
		film1.setReleaseDate(LocalDate.of(1500, 1, 1));
		entityExceptionResponse = restTemplate.exchange(
				new RequestEntity<>(film1, HttpMethod.PUT, URI.create(url)), ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
	}

	@Test
	public void getAllFilms() {
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
	public void getFilmById1() {
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
	public void getFilmById999() {
		ResponseEntity<ExceptionResponse> response =
				restTemplate.getForEntity("/films/999", ExceptionResponse.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.hasBody());
		ExceptionResponse ex = response.getBody();
		assertTrue(ex.getExceptionClass().contains("NotFoundException"));
		assertTrue(ex.getExceptionMessage().contains("[id: 999][Объект по ID не найден]"));
	}
}