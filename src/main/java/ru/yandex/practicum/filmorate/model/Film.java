package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Film {
    @Positive
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank
    private final String name;
    @Size(max=200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Duration duration;
}
