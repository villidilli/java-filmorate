package ru.yandex.practicum.filmorate.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends Requestable {
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();
    @Positive(message = "ID должен быть положительным целым числом")
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @Email(message = "Email несоответствует нормам синтаксиса")
    @NotBlank(message = "Email не может быть null или пустым")
    private String email;
    @PastOrPresent(message = "Дата рождения не должна быть в будущем времени")
    private LocalDate birthday;
}