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

import java.util.List;
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

    @Override
    public List<User> getAll() { //ref
        log.debug("/getAll(user)");
        List<User> users = storage.getAllUsers();
        users.forEach(user -> user.setFriends(getUserFriends(user)));
        return users;
    }

    @Override
    public User getById(Integer userId) { //ref
        log.debug("/getById(user)");
        isExist(userId);
        User user = storage.getById(userId);
        user.setFriends(getUserFriends(user));
        return user;
    }

    public List<User> getFriendsById(Integer userId) { //ref
        log.debug("/getFriendsById");
        isExist(userId);
        List<User> friends = storage.getFriendsAsUser(userId); // получил список юзеров с пустым полем друзья
        friends.forEach(friend -> friend.setFriends(getUserFriends(friend)));
        return friends;
    }

    private List<Friend> getUserFriends(User user) { //ref
        log.debug("getUserFriends");
        List<Friend> friends = storage.getFriendsAsFriend(user.getId());
        friends.forEach(friend -> {
            Boolean status = storage.isMutualFriendship(user.getId(), friend.getId());
            friend.setStatusFriendship(status);
        });
        return friends;
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) { //ref
        log.debug("/getCommonFriends");
        log.debug("income user1-id / user2-id: [" + user1Id + "/" + user2Id + "]");
        isExist(user1Id);
        isExist(user2Id);
        List<User> idFriends = storage.getFriendsAsUser(user1Id);
        List<User> otherIdFriends = storage.getFriendsAsUser(user2Id);
        log.debug(LOG_COMMON_FRIENDS.message, user1Id, user2Id, idFriends);
        List<User> commonFriends = idFriends.stream()
                .filter(otherIdFriends::contains)
                .collect(Collectors.toList());
        commonFriends.forEach(user -> user.setFriends(getUserFriends(user)));
        return commonFriends;
    }

    @Override
    public User create(User user, BindingResult bindResult) { //ref
        log.debug("/create(user)");
        log.debug("income user: " + user.toString());
        customValidate(user);
        annotationValidate(bindResult);
        user.setId(storage.addUserAndReturnId(user));
        return user;
    }

    @Override
    public User update(User user, BindingResult bindResult) { //ref
        log.debug("/update(user)");
        log.debug("income user: " + user.toString());
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
        storage.updateUser(user);
        return getById(user.getId());
    }

    @Override
    protected void customValidate(User user) throws ValidateException { //ref
        log.debug("customValidate(user)");
        log.debug("income user: " + user.toString());
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
    }

    @Override
    protected void isExist(Integer id) { //ref
        log.debug("/isExist(user)");
        log.debug("income id: " + id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }
}