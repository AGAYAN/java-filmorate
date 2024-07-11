package ru.yandex.practicum.filmorate.service;

public interface FilmServiceInterface {
    void likeFilm(Long id, Long userId);

    void unlikeFilm(Long id, Long userId);
}
