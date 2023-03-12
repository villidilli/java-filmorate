package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
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

@Service
@Slf4j
public class UserService extends ServiceRequestable<User> {
    private final UserStorage storage;

    @Autowired
    private UserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User create(User user, BindingResult bindResult) {
        log.debug("/create(user)");
        log.debug("income user: " + user.toString());
        customValidate(user);
        annotationValidate(bindResult);
        user.setId(storage.addAndReturnId(user));
        return user;
    }

    @Override
    public User update(User user, BindingResult bindResult) {
        log.debug("/update(user)");
        log.debug("income user: " + user.toString());
        annotationValidate(bindResult);
        customValidate(user);
        isExist(user.getId());
        storage.update(user);
        return getById(user.getId());
    }

    @Override
    public List<User> getAll() { //ref
        log.debug("/getAll(user)");
        List<User> users = storage.getAll();
        users.forEach(user -> user.setFriends(getUserFriends(user)));
        return users;
    }

    @Override
    public User getById(Integer userId) {
        log.debug("/getById(user)");
        log.debug("income user id: " + userId);
        isExist(userId);
        User user = storage.getById(userId);
        user.setFriends(getUserFriends(user));
        return user;
    }

    public List<User> getFriendsById(Integer userId) {
        log.debug("/getFriendsById");
        log.debug("income user id: " + userId);
        isExist(userId);
        List<User> friends = storage.getFriendsAsUser(userId);
        log.debug("list user friends :" + friends);
        friends.forEach(friend -> friend.setFriends(getUserFriends(friend)));
        return friends;
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        log.debug("/getCommonFriends");
        log.debug("income user1-id / user2-id: [" + user1Id + "/" + user2Id + "]");
        isExist(user1Id);
        isExist(user2Id);
        List<User> user1friends = storage.getFriendsAsUser(user1Id);
        log.debug("list friends user1: " + user1friends);
        List<User> user2friends = storage.getFriendsAsUser(user2Id);
        log.debug("list friends user2: " + user2friends);
        List<User> commonFriends = user1friends.stream()
                .filter(user2friends::contains)
                .collect(Collectors.toList());
        log.debug("common friends: " + commonFriends);
        commonFriends.forEach(user -> user.setFriends(getUserFriends(user)));
        return commonFriends;
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
    protected void customValidate(User user) throws ValidateException {
        log.debug("customValidate(user)");
        log.debug("income user: " + user.toString());
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
    }

    @Override
    protected void isExist(Integer id) {
        log.debug("/isExist(user)");
        log.debug("income id: " + id);
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (storage.getById(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
    }

    protected Boolean getStatusFriendship(Integer userId, Integer checkedUserId) {
        log.debug("getStatusFriendship");
        log.debug("income userId / checkedUserId [" + userId + "/" + checkedUserId + "]");
        List<Integer> userMutualFriends = storage.getMutualFriendsId(userId);
        log.debug("user mutual friendsId: " + userMutualFriends);
        return userMutualFriends.contains(checkedUserId);
    }

    private List<Friend> getUserFriends(User user) {
        log.debug("getUserFriends");
        log.debug("income user: " + user.toString());
        List<Friend> friends = storage.getFriendsAsFriend(user.getId());
        log.debug("list user friends :" + friends);
        friends.forEach(friend -> {
            Boolean status = getStatusFriendship(user.getId(), friend.getId());
            friend.setStatusFriendship(status);
        });
        return friends;
    }
}