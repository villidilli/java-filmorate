package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

@Slf4j
public abstract class ServiceRequestable<T extends Requestable> {

    public abstract List<T> getAll();

    public abstract T create(T t, BindingResult bindResult);

    public abstract T update(T t, BindingResult bindResult);

    public abstract T getById(Integer id);

    protected abstract void customValidate(T t);

    protected void annotationValidate(BindingResult bindResult) throws ValidateException {
        log.debug("/annotationValidate");
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
    }

    protected abstract void isExist(Integer id);

    private String collectBindResultMessage(BindingResult bindResult) {
        log.debug("/collectBindResultMessage");
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }
}