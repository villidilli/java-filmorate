package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.*;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.controller.FilmController.BIRTHDAY_CINEMA;
import static ru.yandex.practicum.filmorate.exception.ValidationException.RELEASE_DATE_INVALID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film extends Requestable implements Validatable {
    @Positive
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Long duration;
}
