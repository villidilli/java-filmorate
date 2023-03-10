package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmIdComparator implements Comparator<Film> {
	@Override
	public int compare(Film o1, Film o2) {
		return o1.getId().compareTo(o2.getId());
	}

	@Override
	public Comparator<Film> reversed() {
		return Comparator.super.reversed();
	}
}
