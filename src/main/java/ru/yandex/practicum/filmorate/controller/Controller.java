package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.*;

import static ru.yandex.practicum.filmorate.util.Message.*;

@Slf4j
public abstract class Controller<T extends Requestable> {
    protected abstract void customValidate(T obj) throws ValidateException;
    protected abstract void addInStorage(T obj);
    protected abstract void updateInStorage(T obj);

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }

    protected void annotationValidate(BindingResult bindResult) {
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
        log.debug(LOG_ANNOTATION_VALID_SUCCESS.message);
    }

    public abstract List<T> getAll();

    public T create(T obj, BindingResult bindResult) throws ValidateException {
        customValidate(obj);
        annotationValidate(bindResult);
        addInStorage(obj);
        return obj;
    }

    public T update(T obj, BindingResult bindResult) throws ValidateException{
        annotationValidate(bindResult);
        customValidate(obj);
        updateInStorage(obj);
        return obj;
    }
}