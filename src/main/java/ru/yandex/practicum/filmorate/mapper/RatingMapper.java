package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMapper {
    public static Mpa mapRow(ResultSet rs, int mapRow) throws SQLException {
        return new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"));
    }
}

