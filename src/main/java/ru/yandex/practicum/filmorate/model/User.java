package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends Requestable {
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