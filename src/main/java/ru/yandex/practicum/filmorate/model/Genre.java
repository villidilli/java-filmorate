package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Genre {
	@NotBlank(message = "ID должен быть целым числом больше 0")
	@EqualsAndHashCode.Include
	private Integer id;
	private String name;
}