package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.*;

import static ru.yandex.practicum.filmorate.controller.Message.*;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.*;
import static ru.yandex.practicum.filmorate.exception.ValidateException.*;

@Slf4j
public abstract class Controller<T extends Requestable> {
    @Getter
    protected final Map<Integer, T> objects = new HashMap<>();
    @Getter
    protected int generatorID = 1;

    protected abstract void customValidate(T obj) throws ValidateException;

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
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
        log.debug(LOG_ANNOTATION_VALID_SUCCESS.message);
    }

    private void isObjectExist(T obj) throws ValidateException, NotFoundException {
        Integer id = obj.getId();
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (objects.get(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message);
    }

    public List<Requestable> getAllObjects() {
        return new ArrayList<>(objects.values());
    }

    public Requestable create(T obj, BindingResult bindResult) throws ValidateException {
        customValidate(obj);
        annotationValidate(bindResult);
        obj.setId(generatorID++);
        objects.put(obj.getId(), obj);
        log.debug(LOG_WRITE_OBJECT.message, obj.getClass().getSimpleName());
        log.debug(LOG_SIZE_OBJECTS.message, objects.size());
        return obj;
    }

    public Requestable update(T obj, BindingResult bindResult) throws ValidateException{
        annotationValidate(bindResult);
        customValidate(obj);
        isObjectExist(obj);
        log.info(LOG_VALIDATION_SUCCESS.message);
        objects.put(obj.getId(), obj);
        log.debug(LOG_WRITE_OBJECT.message, obj.getClass().getSimpleName());
        return obj;
    }
}