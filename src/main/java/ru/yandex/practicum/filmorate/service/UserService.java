package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;

import ru.yandex.practicum.filmorate.dao.UserFriendStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidateException.LOGIN_NOT_HAVE_SPACE;

@Service
@Slf4j
public class UserService extends ServiceRequestable<User> {
    private final UserStorage storage;
    private final UserFriendStorage friendStorage;

    @Autowired
    private UserService(UserStorage storage, UserFriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }

    @Override
    public User create(User user, BindingResult bindResult) {
        log.debug("/create(user)");
        log.debug("income user: {}", user.toString());
        customValidate(user);
        annotationValidate(bindResult);
        user.setId(storage.addAndReturnId(user));
        return storage.getById(user.getId());
    }

    @Override
    public User update(User user, BindingResult bindResult) {
        log.debug("/update(user)");
        log.debug("income user: {}", user.toString());
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
        storage.update(user);
        return getById(user.getId());
    }

    @Override
    public List<User> getAll() {
        log.debug("/getAll(user)");
        return storage.getAll();
    }

    @Override
    public User getById(Integer userId) {
        log.debug("/getById(user)");
        log.debug("income user id: {}", userId);
        isExist(userId);
        return storage.getById(userId);
    }

    public List<User> getFriendsById(Integer userId) {
        log.debug("/getFriendsById");
        log.debug("income user id: {}", userId);
        isExist(userId);
        return storage.getFriendsByUser(userId);
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        log.debug("/getCommonFriends");
        log.debug("user1Id: {}, userId2: {}", user1Id, user2Id);
        isExist(user1Id);
        isExist(user2Id);
        return storage.getCommonFriends(user1Id, user2Id);
    }

    public void addFriend(int userId, int friendId) {
        log.debug("/addFriend");
        log.debug("userID: {}, friendId: {}", userId, friendId);
        isExist(userId);
        isExist(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.debug("/deleteFriend");
        log.debug("userId: {}, friendId: {}", userId, friendId);
        isExist(userId);
        isExist(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    @Override
    protected void customValidate(User user) throws ValidateException {
        log.debug("customValidate(user)");
        log.debug("income user: {}", user.toString());
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
    }

    @Override
    protected void isExist(Integer id) {
        log.debug("/isExist(user)");
        log.debug("income id: {}", id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (!storage.isExist(id)) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}