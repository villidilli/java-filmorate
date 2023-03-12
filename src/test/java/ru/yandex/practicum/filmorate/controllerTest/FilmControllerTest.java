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
		Mpa mpa1 = new Mpa();
		mpa1.setId(1);
		film1.setMpa(mpa1);
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
		Mpa mpa2 = new Mpa();
		mpa2.setId(2);
		film1.setMpa(mpa2);
		Genre genre3 = new Genre();
		genre3.setId(3);
		Genre genre4 = new Genre();
		genre2.setId(4);
		film1.setGenres(List.of(genre3, genre4));

		film3 = new Film();
		film3.setName("О войне");
		film3.setDescription("История о войне");
		film3.setReleaseDate(LocalDate.of(1980, 12, 31));
		film3.setDuration(220L);
		Mpa mpa3 = new Mpa();
		mpa3.setId(3);
		film1.setMpa(mpa3);
		Genre genre5 = new Genre();
		genre5.setId(5);
		Genre genre6 = new Genre();
		genre6.setId(6);
		film1.setGenres(List.of(genre5, genre6));
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
		ResponseEntity<List<Film>> response = restTemplate.exchange(
						"/films", HttpMethod.GET, null, listFilmType);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.hasBody());
		assertEquals(2, response.getBody().size());


	}
}