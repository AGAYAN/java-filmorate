package ru.yandex.practicum.filmorate.storage.filmStorage;

import lombok.RequiredArgsConstructor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreStorage;

    @Override
    public Film add(Film film) {
        final String sqlInsertFilm = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsertFilm, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            throw new NotFoundException("Не удалось получить ID нового фильма.");
        }
        film.setId(generatedId.longValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            associateFilmWithGenres(film);
        }

        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM films " +
                "left join mpa on films.mpa_id = mpa.mpa_id " +
                "LEFT JOIN filmgenres ON films.film_id = filmgenres.film_id " +
                "LEFT JOIN genres ON filmgenres.genre_id = genres.genre_id " +
                "LEFT JOIN likes ON likes.film_id = films.film_id;";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmMapper::mapRow);
        genreStorage.load(films);
        return films;
    }

    @Override
    public Film findById(Long id) {
        log.info("Фильм с id = {} ", id);
        String sqlQuery = "select * from films " +
                "left join mpa on films.mpa_id = mpa.mpa_id " +
                "where films.film_id = ?;";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, FilmMapper::mapRow, id);
            final String sqlQueryGenres = "select * from FILMGENRES " +
                    "left join genres " +
                    "on FILMGENRES.genre_id = genres.genre_id " +
                    "where FILMGENRES.film_id = ?;";
            film.setGenres(new HashSet<>(jdbcTemplate.query(sqlQueryGenres, GenreMapper::mapRow, id)));
            return film;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public Film update(Film film) {
        final String sqlQuery = "UPDATE films SET " +
                "name = ?, " +
                "description = ?, " +
                "releaseDate = ?, " +
                "duration = ? " +
                "WHERE film_id = ?;";
        int temp = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        if (temp == 0) {
            throw new NotFoundException("Невозможно обновить фильм с id = " + film.getId());
        }
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        String deleteGenreSql = "DELETE FROM filmgenres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenreSql, id);
        String sqlQuery = "delete from films where film_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Удаление фильма c id = {}", id);
    }

    @Override
    public void takeLike(Long id, Long userId) {
        final String sqlQuery = "insert into likes (film_id,user_id) values (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pr = con.prepareStatement(sqlQuery);
            pr.setLong(1, id);
            pr.setLong(2, userId);
            return pr;
        });
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        final String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public List<Film> getPopularFilm(Long limit) {
        log.info("Fetching popular films with limit: {}", limit);
        String sqlQuery = "SELECT f.*, m.*, COUNT(l.user_id) AS likes_count " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmMapper::mapRow, limit);
        genreStorage.load(films);
        return films;
    }

    private void associateFilmWithGenres(Film film) {
        String sqlInsertGenreAssociation = "INSERT INTO filmgenres (film_id, genre_id) VALUES (?, ?)";

        List<Object[]> batchArgs = film.getGenres().stream()
                .map(genre -> new Object[]{film.getId(), genre.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sqlInsertGenreAssociation, batchArgs);
    }

}
