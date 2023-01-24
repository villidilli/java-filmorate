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
    @NotBlank
    private String login;
    private String name;
    @Email
    @NotBlank
    private String email;
    @Past
    private LocalDate birthday;
}
