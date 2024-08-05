package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenresById(Long id);

    List<Genre> getAllGenres();

    void load(List<Film> films);

}
