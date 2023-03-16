package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.exception.MapperException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class GenresExtractor implements ResultSetExtractor<Map<Integer, List<Genre>>> {
    private final GenreMapper genreMapper = new GenreMapper();
    private List<Genre> genres = new ArrayList<>();
    private final Map<Integer, List<Genre>> idFilmListGenres = new HashMap<>();
    private final Comparator<Genre> compId = Comparator.comparing(Genre::getId);


    @Override
    public Map<Integer, List<Genre>> extractData(ResultSet rs) {
        log.debug("/extractData(Genres)");
        try {
            while (rs.next()) {
                Genre genre = genreMapper.mapRow(rs, rs.getRow());
                int filmId = rs.getInt("id_film");
                genres = idFilmListGenres.getOrDefault(filmId, new ArrayList<>());
                genres.add(genre);
                genres.sort(compId);
                idFilmListGenres.put(filmId, genres);
            }
        } catch (SQLException e) {
            throw new MapperException("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
        return idFilmListGenres;
    }
}