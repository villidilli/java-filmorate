package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.MpaMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.DbQuery.*;
import static ru.yandex.practicum.filmorate.exception.NotFoundException.NOT_FOUND_BY_ID;

@Repository
@Slf4j
public class MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaStorage(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<Mpa> getAll() {
        log.debug("/getAllMpa");
        try {
            return jdbcTemplate.query(MPA_GET_ALL.query, new MpaMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Mpa getById(Integer mpaId) {
        log.debug("/getById");
        log.debug("income id: {}", mpaId);
        try {
            return jdbcTemplate.queryForObject(MPA_GET_BY_ID.query, new MpaMapper(), mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("[id: " + mpaId + "]" + NOT_FOUND_BY_ID);
        }
    }
}