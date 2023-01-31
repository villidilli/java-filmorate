package ru.yandex.practicum.filmorate.controller;

public enum Message {
	LOG_ID_GEN("ID generator [{}]"),
	LOG_WRITE_OBJECT("Записан объект: [{}]"),
	LOG_SIZE_USERS("Всего пользователей: [{}]"),
	LOG_VALIDATION_SUCCESS("Валидация пройдена успешно"),
	LOG_SIZE_OBJECTS("Всего объектов: [{}]");

	final String message;

	Message(String message) {
		this.message = message;
	}
}