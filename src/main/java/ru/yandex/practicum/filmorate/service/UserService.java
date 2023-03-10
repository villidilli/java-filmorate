package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
        storage.deleteFriend(id, friendId);
    }

    public List<User> getFriendsById(Integer id) {
        log.debug("/getFriendsById");
        isExist(id);
//        return storage.getFriends(id);
//        return storage.getById(id).getFriends().stream().map(storage::getById).collect(Collectors.toList());
        List<User> friends = storage.getFriendsAsUser(id); // получил список юзеров с пустым полем друзья
        friends.forEach(this::collectFriends);
        return friends;

    }

    private void collectFriends(User user) {
        List<Friend> friendsIds = storage.getFriendsAsFriend(user.getId());
        friendsIds.forEach(friend -> {
            Boolean status = storage.getStatusFriendShip(user.getId(), friend.getId());
            friend.setStatusFriendship(status);
        });
        user.setFriends(friendsIds);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        log.debug("/getCommonFriends");
        isExist(id);
        isExist(otherId);
        List<User> idFriends = storage.getFriendsAsUser(id);
        List<User> otherIdFriends = storage.getFriendsAsUser(otherId);
        log.debug(LOG_COMMON_FRIENDS.message, id, otherId, idFriends);
        List<User> commonFriends = idFriends.stream()
                .filter(otherIdFriends::contains)
                .collect(Collectors.toCollection(ArrayList::new));
        commonFriends.forEach(this::collectFriends);
        return commonFriends;
    }

    @Override
    public List<User> getAllFilms() {
        log.debug("/getAll");
//        return storage.getAll();
        List<User> backedUsers = storage.getAll();
        backedUsers.forEach(this::collectFriends);
        return backedUsers;
    }

    @Override
    public User create(User user, BindingResult bindResult) {
        log.debug("/create");
        customValidate(user);
        annotationValidate(bindResult);
        return storage.add(user);
    }

    @Override
    public User update(User user, BindingResult bindResult) {
        log.debug("/update");
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
//        return storage.update(user);
        User backedUser = storage.update(user);
        collectFriends(backedUser);
        return backedUser;
    }




    @Override
    public User getById(Integer id) { //todo создать индекс
        log.debug("/getById");
        isExist(id);
//        return storage.getById(id);
        User backedUser = storage.getById(id);
        collectFriends(backedUser);
        return backedUser;
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
}