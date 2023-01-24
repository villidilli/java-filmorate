package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


public class FilmControllerTest {
	FilmController controller;
	private ResponseEntity<Film> response;
	private Film film1;
	private Film actualFilm;

	@BeforeEach
	public void beforeEach() {
		System.err.close();
		System.setErr(System.out);

		controller = new FilmController();
		film1 = new Film();
		film1.setName("Титаник");
		film1.setDescription("История о пароходе");
		film1.setReleaseDate(LocalDate.of(2000, 1, 1));
		film1.setDuration(140L);
	}

	@Test
	public void shouldReturnStatus400AndBodyWhenReleaseDateBeforeCreateMethod() {
		film1.setReleaseDate(LocalDate.of(1500, 1, 1));
		response = controller.create(film1);
		actualFilm = response.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(actualFilm.getId());
		assertEquals(film1.getName(), actualFilm.getName());
		assertEquals(film1.getDescription(), actualFilm.getDescription());
		assertEquals(film1.getReleaseDate(), actualFilm.getReleaseDate());
		assertEquals(film1.getDuration(), film1.getDuration());
	}

	@Test
	public void shouldReturnStatus400AndBodyWhenReleaseDateBeforeUpdateMethod() {
		controller.create(film1);
		film1.setReleaseDate(LocalDate.of(1500, 1, 1));
		film1.setId(1);
		response = controller.update(film1);
		actualFilm = response.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(1, actualFilm.getId());
		assertEquals(film1.getName(), actualFilm.getName());
		assertEquals(film1.getDescription(), actualFilm.getDescription());
		assertEquals(film1.getReleaseDate(), actualFilm.getReleaseDate());
		assertEquals(film1.getDuration(), film1.getDuration());
	}
}