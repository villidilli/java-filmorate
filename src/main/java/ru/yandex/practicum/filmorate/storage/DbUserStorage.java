package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component("DbUserStorage")
public class DbUserStorage extends DbRequestableStorage<User> {
}