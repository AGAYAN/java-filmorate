package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void add(Film film);

    Film findById(Long id);

    void update(Film film);

    Collection<Film> findAll();

    void likeFilm(Long id, Long userId);

    void unlikeFilm(Long id, Long userId);
}