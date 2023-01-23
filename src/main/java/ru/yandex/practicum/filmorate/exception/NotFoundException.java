package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException{

	public static final String NOT_FOUND = "Запрашиваемый объект не найден";
	public NotFoundException(String message) {
		super(message);
	}
}
