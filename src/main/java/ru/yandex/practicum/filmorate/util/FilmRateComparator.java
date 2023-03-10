package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmRateComparator implements Comparator<Film> {
	@Override
	public int compare(Film o1, Film o2) {
		return o2.getRate().compareTo(o1.getRate());
	}

	@Override
	public Comparator<Film> reversed() {
		return Comparator.super.reversed();
	}
}
