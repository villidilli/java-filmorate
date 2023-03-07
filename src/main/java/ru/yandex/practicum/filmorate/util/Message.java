package ru.yandex.practicum.filmorate.util;

public enum Message {
	LOG_WRITE_OBJECT("Записан объект: [{}]"),
	LOG_ANNOTATION_VALID_SUCCESS("@Valid-валидация пройдена успешно"),
	LOG_CUSTOM_VALID_SUCCESS("Кастом-валидация пройдена успешно"),
	LOG_IS_EXIST_SUCCESS("Запрашиваемый объект найден: ID [{}]"),
	LOG_SIZE_OBJECTS("Количество объектов: [{}]"),
	LOG_UPDATE_OBJECT("Обновлен объект: [{}]"),
	LOG_DELETE_FRIEND("User [{}]: Friend [{}] удален"),
	LOG_FRIEND("Пользователю [{}]: добавлен друг [{}]"),
	LOG_COMMON_FRIENDS("Users [{}]-[{}]: общие друзья [{}]"),
	LOG_ADD_LIKE("User [{}] лайкнул Film [{}]"),
	LOG_DELETE_LIKE("User [{}] удалил лайк Film [{}]"),
	LOG_POPULAR_FILMS("Фильмы по популярность (count = [{}]) : [{}]");

	public final String message;

	Message(String message) {
		this.message = message;
	}
}