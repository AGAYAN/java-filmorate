package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film findById(Long id);

    Film update(Film film);

    Collection<Film> findAll();

    void deleteFilm(Long id);

    void takeLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopularFilm(Long limit);
}