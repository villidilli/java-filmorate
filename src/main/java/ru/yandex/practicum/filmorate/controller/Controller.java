package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ThrowableException;
import ru.yandex.practicum.filmorate.model.Requestable;

import javax.validation.ValidationException;

import java.util.*;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;

@Slf4j
public abstract class Controller<T extends Requestable> {
    protected final Map<Integer, T> objects = new HashMap<>();
    protected int generatorID = 1;

    protected abstract void customValidate(T obj) throws ValidationException;

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }

    private void annotationValidate(BindingResult bindResult) {
        if (bindResult.hasErrors()) throw new ValidationException(collectBindResultMessage(bindResult));
    }

    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }

    private void isObjectExist(T obj) throws ValidationException, NotFoundException {
        Integer id = obj.getId();
        if (id == null) throw new ValidationException("[id] " + ID_NOT_IS_BLANK);
        if (objects.get(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }

    protected ExceptionResponse exceptionHandler(ValidationException e) {
        log.debug("/handlerValidationException");
        logException(HttpStatus.BAD_REQUEST, e);
        return new ExceptionResponse(e);
    }

    protected ExceptionResponse exceptionHandler(NotFoundException e) {
        log.debug("/handlerNotFoundException");
        logException(HttpStatus.NOT_FOUND, e);
        return new ExceptionResponse(e);
    }

    protected ExceptionResponse exceptionHandler(ThrowableException e) {
        log.debug("/handlerTrowableException");
        logException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        return new ExceptionResponse(e);
    }

    public List<Requestable> getAllObjects() {
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return new ArrayList<>(objects.values());
    }

    public Requestable create(T obj, BindingResult bindResult) throws ValidationException {
        customValidate(obj);
        annotationValidate(bindResult);
        log.info(LOG_VALIDATION_SUCCESS.message);
        obj.setId(generatorID++);
        objects.put(obj.getId(), obj);
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return obj;
    }

    public Requestable update(T obj, BindingResult bindResult) throws ValidationException{
        annotationValidate(bindResult);
        customValidate(obj);
        isObjectExist(obj);
        log.info(LOG_VALIDATION_SUCCESS.message);
        objects.put(obj.getId(), obj);
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return obj;
    }
}