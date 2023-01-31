package ru.yandex.practicum.filmorate.controller;

import java.time.LocalTime;

public class ExceptionResponse {
	String exceptionClass;
	LocalTime timestamp;
	String exceptionMessage;

	public ExceptionResponse(RuntimeException exception) {
		exceptionClass = exception.getClass().getSimpleName();
		timestamp = LocalTime.now();
		exceptionMessage = exception.getMessage();
	}
}
