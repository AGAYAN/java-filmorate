package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatusMapper {
    public static Status mapRow(ResultSet rs, int rowMap) throws SQLException {
        return new Status(rs.getLong("id"),
                rs.getString("status_name"));
    }
}

