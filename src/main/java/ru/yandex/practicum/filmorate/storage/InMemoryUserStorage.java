package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;
import static ru.yandex.practicum.filmorate.exception.ValidateException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.util.Message.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    protected final Map<Integer, User> users = new HashMap<>();
    protected Integer generatorId = 1;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        user.setId(generatorId++);
        users.put(user.getId(), user);
        log.debug(LOG_WRITE_OBJECT.message, user.getClass().getSimpleName());
        return user;
    }

    @Override
    public User update(User user) {
        isExist(user);
        users.put(user.getId(), user);
        log.debug(LOG_WRITE_OBJECT.message, user.getClass().getSimpleName());
        return null;
    }

    private void isExist(User user) {
        Integer id = user.getId();
        if (id == null) throw new ValidateException("[id] " + ID_NOT_IS_BLANK);
        if (users.get(id) == null) throw new NotFoundException("[id: " + id + "]" + NOT_FOUND_BY_ID);
        log.debug(LOG_IS_EXIST_SUCCESS.message);
    }
}
