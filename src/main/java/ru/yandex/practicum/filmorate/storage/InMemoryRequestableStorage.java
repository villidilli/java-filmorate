package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.Requestable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Message.*;

@Slf4j
public abstract class InMemoryRequestableStorage<T extends Requestable> implements RequestableStorage<T> {
    protected final Map<Integer, T> objects = new HashMap<>();
    protected Integer generatorId = 1;

    public List<T> getAll() {
        log.debug(LOG_SIZE_OBJECTS.message, objects.size());
        return new ArrayList<>(objects.values());
    }

    public T add(T t) {
        t.setId(generatorId++);
        objects.put(t.getId(), t);
        log.debug(LOG_WRITE_OBJECT.message, t);
        return t;
    }

    public T update(T t) {
        objects.put(t.getId(), t);
        log.debug(LOG_UPDATE_OBJECT.message, t);
        return t;
    }

    public T getById(Integer id) {
        return objects.get(id);
    }
}