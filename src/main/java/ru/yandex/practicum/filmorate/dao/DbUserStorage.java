package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository("DbUserStorage")
public class DbUserStorage extends DbRequestableStorage<User> {
}