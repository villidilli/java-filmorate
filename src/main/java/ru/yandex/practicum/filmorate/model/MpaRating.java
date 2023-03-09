package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
public class MpaRating {
	@NotBlank(message = "ID должен быть целым числом больше 0")
	private Integer id;
	private String name;
}