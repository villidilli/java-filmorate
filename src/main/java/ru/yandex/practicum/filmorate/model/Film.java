package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    @Positive
    private final int id;
    @NotBlank
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Duration duration;
}
