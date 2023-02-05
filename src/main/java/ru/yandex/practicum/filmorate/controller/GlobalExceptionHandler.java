package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice("ru.yandex.practicum")
@Slf4j
public class GlobalExceptionHandler {
    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ExceptionResponse exceptionHandler(ValidateException e) {
        log.debug("/handlerValidationException");
        logException(HttpStatus.BAD_REQUEST, e);
        return new ExceptionResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ExceptionResponse exceptionHandler(NotFoundException e) {
        log.debug("/handlerNotFoundException");
        logException(HttpStatus.NOT_FOUND, e);
        return new ExceptionResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionResponse exceptionHandler(Exception e) {
        log.debug("/handlerUnexpectedException");
        logException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        return new ExceptionResponse(e);
    }
}