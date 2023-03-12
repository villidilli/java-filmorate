package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private long duration;
    private Mpa mpa = new Mpa();
    private List<Genre> genres = new ArrayList<>();
    @JsonIgnore
    private Integer rate;
}