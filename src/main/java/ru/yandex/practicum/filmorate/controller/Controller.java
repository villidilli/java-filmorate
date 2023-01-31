package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Requestable;

import javax.validation.ValidationException;

import java.time.LocalTime;
import java.util.*;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;

@Slf4j
public abstract class Controller<T extends Requestable> {
    protected final Map<Integer, T> objects = new HashMap<>();
    protected int generatorID = 1;

    protected abstract void validate(T obj) throws ValidationException;

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }

    private Map<String, String> collectResponseBody(Exception e) {
        return Map.of(
                "exception", e.getClass().getSimpleName(),
                "message", e.getMessage(),
                "time", LocalTime.now().toString()
        );
    }

    private void validateBindResult(BindingResult bindResult) {
        if (bindResult.hasErrors()) throw new ValidationException(collectBindResultMessage(bindResult));
    }

    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }

    private void isExist(T obj) throws ValidationException, NotFoundException {
        Integer id = obj.getId();
        if (id == null) throw new ValidationException("[id] " + ID_NOT_IS_BLANK);
        if (objects.get(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }

    protected ResponseEntity<Map<String, String>> exceptionHandler(ValidationException e) {
        log.debug("/handlerValidationException");
        logException(HttpStatus.BAD_REQUEST, e);
        return ResponseEntity.badRequest().body(collectResponseBody(e));
    }

    protected ResponseEntity<Map<String, String>> exceptionHandler(NotFoundException e) {
        log.debug("/handlerNotFoundException");
        logException(HttpStatus.NOT_FOUND, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(collectResponseBody(e));
    }

    public List<Requestable> getAllObjects() {
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return new ArrayList<>(objects.values());
    }

    public ResponseEntity<Requestable> create(T obj, BindingResult bindResult) throws ValidationException {
        validateBindResult(bindResult);
        validate(obj);
        log.info(LOG_VALIDATION_SUCCESS.message);
        obj.setId(generatorID++);
        objects.put(obj.getId(), obj);
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return ResponseEntity.ok(obj);
    }

    public ResponseEntity<Requestable> update(T obj, BindingResult bindResult) throws ValidationException{
        validateBindResult(bindResult);
        validate(obj);
        isExist(obj);
        log.info(LOG_VALIDATION_SUCCESS.message);
        objects.put(obj.getId(), obj);
        log.info(LOG_SIZE_OBJECTS.message, objects.size());
        return ResponseEntity.ok(obj);
    }
}