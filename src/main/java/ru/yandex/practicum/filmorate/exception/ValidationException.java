package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException{
	public static final String LOGIN_NOT_HAVE_SPACE = "Логин не должен содержать пробелы";
	public static final String ID_NOT_IS_BLANK = "ID не указан";
	public static final String RELEASE_DATE_INVALID = "Дата выпуска фильма раньше, чем день рождения кино";

	public ValidationException(String message) {
		super(message);
	}
}