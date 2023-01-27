package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;


@RestController
@Slf4j
@Validated
@RequestMapping("/users")
public class UserController extends Controller<User>{
//    private final Map<Integer, User> users = new HashMap<>();
//    private int generatorID = 1;



//    private void validate(User user) throws ValidationException{
//        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
//        if (user.getName() == null) user.setName(user.getLogin());
//    }

//    private void isExist(User user) throws ValidationException {
//        Integer id = user.getId();
//        if (id == null) throw new ValidationException(ID_NOT_IS_BLANK);
//        if (users.get(id) == null) throw new ValidationException(NOT_FOUND);
//    }

    private void logVariablesCondition() {
//        log.info(LOG_SIZE_USERS.message, users.size());
        log.debug(LOG_ID_GEN.message, generatorID);
    }

    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }



    //    @Override
//    public void validate() {
//        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
//        if (user.getName() == null) user.setName(user.getLogin());
//    }

//    @GetMapping
//    public List<User> getAllUsers() {
//        logVariablesCondition();
//        return new ArrayList<>(users.values());
//    }


    //    @PostMapping
//    public ResponseEntity<User> create(@Valid @RequestBody User user) {
//        try {
//            validate(user);
//            log.debug(LOG_VALIDATION_SUCCESS.message);
//            user.setId(generatorID++);
//            users.put(user.getId(), user);
//            log.debug(LOG_WRITE_OBJECT.message, user);
//            logVariablesCondition();
//            return ResponseEntity.ok(user);
//        } catch (ValidationException e) {
//            logException(HttpStatus.BAD_REQUEST, e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
//        }
//    }

//    @PutMapping
//    public ResponseEntity<User> update(@Valid @RequestBody User user) {
//        try {
//            validate(user);
//            isExist(user);
//            log.debug(LOG_VALIDATION_SUCCESS.message);
//            users.put(user.getId(), user);
//            log.debug(LOG_WRITE_OBJECT.message, user);
//            logVariablesCondition();
//            return ResponseEntity.ok(user);
//        } catch (ValidationException e) {
//            logException(HttpStatus.NOT_FOUND, e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
//        }
//    }
}