package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingStorage {
    Mpa getRatingById(Long id);

    List<Mpa> getAllRating();
}
