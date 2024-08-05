package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getGenresById(Long id) {
        log.info("Пытаемся взять жанр с id = {}", id);
        final String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sqlQuery, GenreMapper::mapRow, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("Берём все жанры");
        final String sqlQuery = "SELECT * FROM genres";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreMapper::mapRow);
        log.info("Количество жанров: {}", genres.size());
        return genres;
    }

    @Override
    public void load(List<Film> films) {
        if (films.isEmpty()) {
            return;
        }

        final Map<Long, Film> filmById = films.stream()
                .collect(Collectors.toMap(
                        Film::getId,
                        Function.identity(),
                        (existingValue, newValue) -> existingValue));

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        final String sqlQuery = "SELECT fg.film_id, g.genre_id, g.name_genres AS name FROM genres g " +
                "JOIN filmgenres fg ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id IN (" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            final Film film = filmById.get(rs.getLong("film_id"));
            if (film != null) {
                System.out.println("Adding genre to film: " + film.getId());
                System.out.println("Genres before adding: " + film.getGenres());
                film.addGenre(new Genre(rs.getLong("genre_id"), rs.getString("name")));
            } else {
                System.out.println("Film not found for ID: " + rs.getLong("film_id"));
            }
            return film;
        }, films.stream().map(Film::getId).toArray(Long[]::new));
    }

}