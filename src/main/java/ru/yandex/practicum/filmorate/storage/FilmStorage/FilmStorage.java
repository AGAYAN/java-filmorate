package ru.yandex.practicum.filmorate.storage.FilmStorage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film) throws ValidationException;
    void delete(int id);
    Film findById(Long id);
    List<Film> findAll();
    void validate(Film film) throws ValidationException;
    void likeFilm(Long id, Long userId);
    void unlikeFilm(Long id, Long userId);
}
