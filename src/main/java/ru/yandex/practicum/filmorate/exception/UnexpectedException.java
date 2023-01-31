package ru.yandex.practicum.filmorate.exception;

public class UnexpectedException extends RuntimeException{
    public static final String UNEXPECTED_ERROR = "[Непредвиденная ошибка сервера]";
    public UnexpectedException(String message) {
        super(message);
    }
}
