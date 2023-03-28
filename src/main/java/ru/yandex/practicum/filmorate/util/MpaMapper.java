package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class MpaMapper implements RowMapper<Mpa> {
	@Override
	public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
		log.debug("/mapRow");
		Mpa mpa = new Mpa();
		mpa.setId(rs.getInt("id_mpa"));
		mpa.setName(rs.getString("mpa_name"));
		return mpa;
	}
}