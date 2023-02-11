package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Message.*;

@Component
@Slf4j
public class InMemoryUserStorage implements StorageRequestable<User> {
    protected final Map<Integer, User> users = new HashMap<>();
    protected Integer generatorId = 1;

    @Override
    public List<User> getAll() {
        log.debug(LOG_SIZE_OBJECTS.message, users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        user.setId(generatorId++);
        users.put(user.getId(), user);
        log.debug(LOG_WRITE_OBJECT.message, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug(LOG_UPDATE_OBJECT.message, user);
        return null;
    }

    @Override
    public User getById(Integer id) {
        return users.get(id);
    }
}