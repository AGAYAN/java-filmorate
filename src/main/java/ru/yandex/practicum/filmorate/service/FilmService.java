package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void add(Film film) throws ValidationException {
        filmStorage.add(film);
    }

    public void delete(int id) {
        filmStorage.delete(id);
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(long filmId, long userId) {
        filmStorage.likeFilm(filmId, userId);
    }

    public void unlikeFilm(long filmId, long userId) {
        filmStorage.unlikeFilm(filmId, userId);
    }
}
