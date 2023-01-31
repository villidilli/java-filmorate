package ru.yandex.practicum.filmorate.exception;

public class ThrowableException extends RuntimeException{
    public static final String UNEXPECTED_ERROR = "[Непредвиденная ошибка сервера]";
    public ThrowableException(String message) {
        super(message);
    }
}
