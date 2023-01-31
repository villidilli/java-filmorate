package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ExceptionResponse {
	protected String exceptionClass;
	protected LocalTime timestamp;
	protected String exceptionMessage;

	public ExceptionResponse(Exception exception) {
		exceptionClass = exception.getClass().getSimpleName();
		timestamp = LocalTime.now();
		exceptionMessage = exception.getMessage();
	}
}
