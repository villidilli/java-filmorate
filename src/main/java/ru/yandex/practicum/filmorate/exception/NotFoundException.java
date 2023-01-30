package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public static final String NOT_FOUND_BY_ID = "Запрашиваемый по ID объект не найден";
    public NotFoundException(String message) {
        super(message);
    }
}
