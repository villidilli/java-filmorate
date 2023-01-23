package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Positive
    @EqualsAndHashCode.Include
    private Integer id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
