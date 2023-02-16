package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.ExceptionResponse;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.net.URI;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
	@Autowired
	TestRestTemplate restTemplate;
	Film film;
	String url = "/films";
	Film actualFilm;
	ResponseEntity<Film> entityFilm;
	ResponseEntity<ExceptionResponse> entityExceptionResponse;
	ExceptionResponse exceptionResponse;

	@BeforeEach
	public void beforeEach() {
		film = new Film();
		film.setName("Титаник");
		film.setDescription("История о пароходе");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(140L);
	}

	@Test
	public void shouldReturnStatus400IfReleaseDateInvalidWhenPOST() {
		film.setReleaseDate(LocalDate.of(1500, 1, 1));
		entityExceptionResponse = restTemplate.postForEntity(url, film, ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
	}

	@Test
	public void shouldReturnStatus400AndBodyIfReleaseDateInvalidWhenPUT() {
		film.setReleaseDate(LocalDate.of(1500, 1, 1));
		entityExceptionResponse = restTemplate.exchange(
				new RequestEntity<>(film, HttpMethod.PUT, URI.create(url)), ExceptionResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, entityExceptionResponse.getStatusCode());
		exceptionResponse = entityExceptionResponse.getBody();
		assertEquals(ValidateException.class.getSimpleName(), exceptionResponse.getExceptionClass());
		assertTrue(exceptionResponse.getExceptionMessage().contains("ReleaseDate"));
	}
}