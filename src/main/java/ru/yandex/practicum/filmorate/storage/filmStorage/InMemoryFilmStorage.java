package ru.yandex.practicum.filmorate.storage.filmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private FilmStorage filmStorage;
    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film add(Film film) {
        long nextId = getNextId();
        film.setId(nextId);
        film.setLikes(new HashSet<>());
        filmMap.put(nextId, film);
        log.info("Фильм с ID: {} успешно добавлен", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Фильм с ID: {} успешно обновлен", film.getId());
            return film;
        } else {
            log.error("Ошибка при обновлении фильма с id: {}", film.getId());
            throw new NotFoundException("Фильм с ID: " + film.getId() + " не найден");
        }
    }

    @Override
    public Collection<Film> findAll() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return filmMap.values();
    }

    @Override
    public Film findById(Long id) {
        if (filmMap.containsKey(id)) {
            log.info("Фильм с ID: {} успешно найден", id);
            return filmMap.get(id);
        } else {
            log.error("Ошибка при получении фильма с id: {}", id);
            throw new NotFoundException("Фильм с id: " + id + " не найден");
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
