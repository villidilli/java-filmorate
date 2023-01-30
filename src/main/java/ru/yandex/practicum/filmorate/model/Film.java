package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Film extends Requestable {
    @Positive(message = "ID должен быть положительным целым числом")
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank (message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание должно содержать не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive (message = "Продолжительность должна быть положительным целым числом")
    private Long duration;
}