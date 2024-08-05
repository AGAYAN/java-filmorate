package ru.yandex.practicum.filmorate.storage.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.StatusMapper;
import ru.yandex.practicum.filmorate.model.Status;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusDbStorage implements StatusStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Status getStatusById(Long id) {
        final String sqlQuery = "SELECT * FROM status WHERE id = ?";
        try {
            Status status = jdbcTemplate.queryForObject(sqlQuery, new MapSqlParameterSource[]{new MapSqlParameterSource().addValue("id", id)}, StatusMapper::mapRow);
            return status;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Статус с id = " + id + " не найден");
        }
    }


    @Override
    public List<Status> getAllStatus() {
        log.info("Пытаемся взять все статусы которые есть в базе");
        final String sqlQuery = "select * from rating";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, StatusMapper::mapRow));
    }
}
