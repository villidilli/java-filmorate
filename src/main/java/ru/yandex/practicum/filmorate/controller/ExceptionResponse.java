package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {
	protected String exceptionClass;
	protected String exceptionMessage;
	protected LocalTime timestamp;

	public ExceptionResponse(Exception exception) {
		exceptionClass = exception.getClass().getSimpleName();
		timestamp = LocalTime.now();
		exceptionMessage = exception.getMessage();
	}
}