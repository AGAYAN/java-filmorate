package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreMapper {
    public static Genre mapRow(ResultSet rs, int mapRow) throws SQLException {
        return new Genre(rs.getLong("genres.genre_id"),
                rs.getString("genres.name_genres"));
    }
}
