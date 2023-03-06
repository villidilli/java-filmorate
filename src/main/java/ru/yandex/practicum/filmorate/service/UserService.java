package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryRequestableStorage;
import ru.yandex.practicum.filmorate.storage.RequestableStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.ValidateException.LOGIN_NOT_HAVE_SPACE;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Service
@Slf4j
public class UserService extends ServiceRequestable<User> {
    @Autowired
    private UserService(@Qualifier("DbUserStorage") RequestableStorage<User> storage) {
        super.storage = storage;
    }

    public void addFriend(int id, int friendId) {
        log.debug("/addFriend");
        isExist(id);
        isExist(friendId);
        addFriendship(id, friendId);
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
        log.debug(LOG_FRIEND.message, id, storage.getById(id).getFriends());
        return storage.getById(id).getFriends().stream().map(storage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        log.debug("/getCommonFriends");
        isExist(id);
        isExist(otherId);
        Set<Integer> idFriends = storage.getById(id).getFriends();
        Set<Integer> otherIdFriends = storage.getById(otherId).getFriends();
        log.debug(LOG_COMMON_FRIENDS.message, id, otherId, idFriends);
        return idFriends.stream()
                .filter(otherIdFriends::contains)
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    @Override
    protected void customValidate(User user) throws ValidateException {
        if (user.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    private void addFriendship(Integer id, Integer friendId) {
        storage.getById(id).addFriend(friendId);
        log.debug(LOG_FRIEND.message, id, storage.getById(id).getFriends());
        storage.getById(friendId).addFriend(id);
        log.debug(LOG_FRIEND.message, friendId, storage.getById(friendId).getFriends());
    }

    private void deleteFriendship(Integer id, Integer friendId) {
        storage.getById(id).deleteFriend(friendId);
        log.debug(LOG_DELETE_FRIEND.message, id, friendId);
        storage.getById(friendId).deleteFriend(id);
        log.debug(LOG_DELETE_FRIEND.message, friendId, id);
    }
}