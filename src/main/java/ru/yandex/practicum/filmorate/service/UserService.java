package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.LOGIN_NOT_HAVE_SPACE;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class UserService extends ServiceRequestable<User> {
    private final UserStorage storage;

    @Autowired
    private UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int id, int friendId) {
        log.debug("/addFriend");
        isExist(id);
        isExist(friendId);
        storage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        log.debug("/deleteFriend");
        isExist(id);
        isExist(friendId);
        deleteFriendship(id, friendId);
    }

    public List<User> getFriendsById(Integer id) {
        log.debug("/getFriendsById");
        isExist(id);
        return storage.getFriends(id);
//        return storage.getById(id).getFriends().stream().map(storage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
//        log.debug("/getCommonFriends");
//        isExist(id);
//        isExist(otherId);
//        Set<Integer> idFriends = storage.getById(id).getFriends();
//        Set<Integer> otherIdFriends = storage.getById(otherId).getFriends();
//        log.debug(LOG_COMMON_FRIENDS.message, id, otherId, idFriends);
//        return idFriends.stream()
//                .filter(otherIdFriends::contains)
//                .map(storage::getById)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public List<User> getAll() {
        log.debug("/getAll");
        return storage.getAll();
    }

    @Override
    public User create(User user, BindingResult bindResult) {
        log.debug("/create");
        customValidate(user);
        annotationValidate(bindResult);
        storage.add(user);
        return user;
    }

    @Override
    public User update(User user, BindingResult bindResult) {
        log.debug("/update");
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
        storage.update(user);
        return user;
    }

    @Override
    public User getById(Integer id) {
        log.debug("/getById");
        isExist(id);
        return storage.getById(id);
    }

    @Override
    protected void customValidate(User user) throws ValidateException {
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    protected void isExist(Integer id) {
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message, id);
    }

    private void deleteFriendship(Integer id, Integer friendId) {
//        storage.getById(id).deleteFriend(friendId);
//        log.debug(LOG_DELETE_FRIEND.message, id, friendId);
//        storage.getById(friendId).deleteFriend(id);
//        log.debug(LOG_DELETE_FRIEND.message, friendId, id);
    }
}