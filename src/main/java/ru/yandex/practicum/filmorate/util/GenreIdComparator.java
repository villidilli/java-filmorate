package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;

public class GenreIdComparator implements Comparator<Genre> {
    @Override
    public int compare(Genre o1, Genre o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
