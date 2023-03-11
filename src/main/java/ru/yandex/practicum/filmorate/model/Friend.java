package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Friend {
	@EqualsAndHashCode.Include
	private Integer id;
	private Boolean statusFriendship;
}
