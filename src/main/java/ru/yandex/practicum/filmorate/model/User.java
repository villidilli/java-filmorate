package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.*;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.exception.ValidationException.LOGIN_NOT_HAVE_SPACE;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User extends Requestable{
    @Positive
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank
    private String login;
    private String name;
    @Email
    @NotBlank
    private String email;
    @PastOrPresent
    private LocalDate birthday;
}