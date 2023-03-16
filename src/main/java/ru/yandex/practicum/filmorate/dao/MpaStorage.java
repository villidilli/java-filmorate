package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.MpaMapper;

import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;

@Repository
@Slf4j
public class MpaStorage implements RequestableStorage<Mpa> {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(MPA_TABLE.query)
                .usingGeneratedKeyColumns(MPA_ID.query);
    }

    @Override
    public List<Mpa> getAll() {
        log.debug("/getAllMpa");
        try {
            return jdbcTemplate.query(MPA_GET_ALL.query, new MpaMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Mpa getById(Integer mpaId) {
        log.debug("/getById");
        log.debug("income id: {}", mpaId);
        try {
            return jdbcTemplate.queryForObject(MPA_GET_BY_ID.query, new MpaMapper(), mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + mpaId + "]" + NOT_FOUND_BY_ID);
        }
    }

    @Override
    public int addAndReturnId(Mpa mpa) {
        log.debug("/addFilmAndReturnId");
        log.debug("income mpa: {}", mpa);
        return jdbcInsert.executeAndReturnKey(Map.of("name", mpa.getName())).intValue();
    }

    @Override
    public void update(Mpa mpa) {
        log.debug("/update");
        log.debug("income mpa: {}", mpa);
        jdbcTemplate.update(MPA_UPDATE.query, mpa.getId());
    }
}