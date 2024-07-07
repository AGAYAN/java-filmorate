package ru.yandex.practicum.filmorate.storage.filmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public void add(Film film) {

        film.setId(getNextId());
        filmMap.put(film.getId(), film);
        log.info("Фильм {} добалвен в коллекцию", film.getName());
    }

    @Override
    public void update(Film film) {
        filmMap.put(film.getId(), film);
        log.info("Данные о фильм {} обновлены ", film.getName());
    }

    public Collection<Film> findAll() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return filmMap.values();
    }

    public Film findById(Long id) {
        return filmMap.get(id);
    }

    public void likeFilm(Long id, Long userId) {
        findById(id).getLikes().add(userId);
    }

    public void unlikeFilm(Long id, Long userId) {
        findById(id).getLikes().remove(userId);
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
