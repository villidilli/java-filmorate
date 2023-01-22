package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.User;

public class UserValidator {
//    public static boolean validate(User user) {
//
//    }

    private boolean emailValidator(User user) {
        return !user.getEmail().isBlank() && user.getEmail().contains("@");
    }

    private boolean loginValidator(User user) {
        return !user.getLogin().isBlank() && !user.getLogin().contains("//s");
    }

    private boolean nameValidator(User user) {
        if (user.getName().isBlank()) user.setName(user.getLogin());
        return true;
    }
}
