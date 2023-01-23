package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException{
	public static final String LOGIN_NOT_HAVE_SPACE = "Логин не должен содержать пробелы";
	public static final String ID_NOT_IS_BLANK = "ID не указан";

	public ValidationException(String message) {
		super(message);
	}
}
