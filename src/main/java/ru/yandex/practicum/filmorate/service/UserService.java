package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.LOGIN_NOT_HAVE_SPACE;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User create(User user, BindingResult bindResult) {
        customValidate(user);
        annotationValidate(bindResult);
        storage.add(user);
        return user;
    }

    public User update(User user, BindingResult bindResult) {
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
        storage.update(user);
        return user;
    }

    public void addFriend(int id, int friendId) {
        isExist(id);
        isExist(friendId);
        storage.getById(id).getFriends().add(friendId);
        storage.getById(friendId).getFriends().add(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        isExist(id);
        isExist(friendId);
        storage.getById(id).getFriends().remove(friendId);
        storage.getById(friendId).getFriends().remove(id);
    }

    public List<User> getFriendsById(Integer id) {
        isExist(id);
        return storage.getAll().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        isExist(id);
        isExist(otherId);
        Set<Integer> idFriends = storage.getById(id).getFriends();
        Set<Integer> otherIdFriends = storage.getById(otherId).getFriends();
        idFriends.retainAll(otherIdFriends);
        return idFriends.stream().map(storage::getById).collect(Collectors.toList());
    }

    private void annotationValidate(BindingResult bindResult) throws ValidateException {
        if (bindResult.hasErrors()) throw new ValidateException(collectBindResultMessage(bindResult));
        log.debug(LOG_ANNOTATION_VALID_SUCCESS.message);
    }

    private String collectBindResultMessage(BindingResult bindResult) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = bindResult.getFieldErrors();
        for (FieldError error : errors) {
            sb.append("[" + error.getField() + "] -> [");
            sb.append(error.getDefaultMessage() + "]");
        }
        return sb.toString();
    }

    private void customValidate(User user) throws ValidateException {
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    private void isExist(Integer id) throws ValidateException, NotFoundException {
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message);
    }


}
