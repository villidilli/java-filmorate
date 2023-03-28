package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.exception.MapperException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class FilmMapper implements RowMapper<Film> {
    private final MpaMapper mpaMapper = new MpaMapper();
    @Override
    public Film mapRow(ResultSet rs, int rowNum) {
        log.debug("/mapRow");
        Film film = new Film();
        try {
            film.setId(rs.getInt("id_film"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getLong("duration"));
            film.setMpa(mpaMapper.mapRow(rs, rowNum));
            film.setRate(rs.getInt("rate"));
            return film;
        } catch (SQLException e) {
            throw new MapperException("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }
}