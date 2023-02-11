package ru.yandex.practicum.filmorate.util;

public enum Message {
	LOG_WRITE_OBJECT("Записан объект: [{}]"),
	LOG_VALIDATION_SUCCESS("Валидация пройдена успешно"),
	LOG_ANNOTATION_VALID_SUCCESS("Валидация аннотациями пройдена успешно"),
	LOG_CUSTOM_VALID_SUCCESS("Валидация кастомная пройдена успешно"),
	LOG_IS_EXIST_SUCCESS("Запрашиваемый объект найден: ID [{}]"),
	LOG_SIZE_OBJECTS("Количество объектов: [{}]"),
	LOG_UPDATE_OBJECT("Обновлен объект: [{}]");

	public final String message;

	Message(String message) {
		this.message = message;
	}
}