package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;


import java.time.LocalDate;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void add(Film film) throws ValidationException {
        validate(film);
        filmStorage.add(film);
    }

    public void delete(int id) {
        filmStorage.delete(id);
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public Map<Long, Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(long filmId, long userId) throws ValidationException {
        checkContains(filmId);
        filmStorage.likeFilm(filmId, userId);
    }

    public void unlikeFilm(long filmId, long userId) throws ValidationException {
        checkContains(filmId);
        filmStorage.unlikeFilm(filmId, userId);
    }

    private void checkContains(Long filmId) throws ValidationException {

        if (filmStorage.findById(filmId) == null) {
            throw new ValidationException("Фильм с id = " + filmId + " не найден");
        }
    }

    public void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
