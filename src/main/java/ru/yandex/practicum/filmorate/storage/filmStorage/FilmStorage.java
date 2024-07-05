package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Film add(Film film);

    void delete(int id);

    Film findById(Long id);

    Map<Long, Film> findAll();

    void likeFilm(Long id, Long userId);

    void unlikeFilm(Long id, Long userId);
}