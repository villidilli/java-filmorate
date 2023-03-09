package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Mpa {
	@NotBlank(message = "ID должен быть целым числом больше 0")
	private Integer id;
	private String name;
}