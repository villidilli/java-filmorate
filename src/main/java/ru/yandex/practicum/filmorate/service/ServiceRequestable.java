package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.Message.*;

@Slf4j
public abstract class ServiceRequestable<T extends Requestable> {
//    protected RequestableStorage<T> storage;

    public abstract List<T> getAllFilms();
//    {
//        log.debug("/getAll");
//        return storage.getAll();
//    }

    public abstract T create(T t, BindingResult bindResult);
//    {
//        log.debug("/create");
//        customValidate(t);
//        annotationValidate(bindResult);
//        storage.add(t);
//        return t;
//    }

    public abstract T update(T t, BindingResult bindResult);
//    {
//        log.debug("/update");
//        annotationValidate(bindResult);
//        customValidate(t);
//        isExist(t.getId());
//        storage.update(t);
//        return t;
//    }

    public abstract T getById(Integer id);
//    {
//        log.debug("/getById");
//        isExist(id);
//        return storage.getById(id);
//    }

    protected abstract void customValidate(T t);

    protected void annotationValidate(BindingResult bindResult) throws ValidateException {
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
        log.debug(LOG_ANNOTATION_VALID_SUCCESS.message);
    }

    protected abstract void isExist(Integer id);
//            throws ValidateException, NotFoundException
//    {
//        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
//        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
//        log.debug(LOG_IS_EXIST_SUCCESS.message, id);
//    }

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }
}