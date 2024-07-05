package ru.yandex.practicum.filmorate.storage.filmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(getNextId());
        filmMap.put(getNextId(), film);

        return film;
    }

    @Override
    public void delete(int id) {
        Film filmToRemove = filmMap.remove(id);
        if (filmToRemove == null) {
            throw new IllegalArgumentException("Фильм с таким ID не найден");
        }
    }

    @Override
    public Film findById(Long id) {
        return filmMap.get(id);
    }

    @Override
    public Map<Long, Film> findAll() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return filmMap;
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Film film = filmMap.get(filmId);
        if (film != null) {
            findById(filmId).getLikes().add(userId);
        } else {
            throw new IllegalArgumentException("Такой фильм с таким id сушествует: " + filmId);
        }
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Film film = filmMap.get(filmId);
        if (film != null) {
            findById(filmId).getLikes().remove(userId);
        } else {
            throw new IllegalArgumentException("Такой фильм с таким id сушествует: " + filmId);
        }
    }

    private long getNextId() {
        long currentMaxId = filmMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
