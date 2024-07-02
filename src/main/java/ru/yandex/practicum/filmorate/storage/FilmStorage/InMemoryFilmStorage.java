package ru.yandex.practicum.filmorate.storage.FilmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();
    private long nextId = 0;

    @Override
    public Film add(Film film) throws ValidationException {
        validate(film);
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
    public List<Film> findAll() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return (List<Film>) filmMap;
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

    @Override
    public void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    private long getNextId() {
        return ++nextId;
    }
}
