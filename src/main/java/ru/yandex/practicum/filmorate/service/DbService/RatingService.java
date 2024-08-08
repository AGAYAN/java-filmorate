package ru.yandex.practicum.filmorate.service.DbService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RatingService {
    private RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingDbStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Mpa getRatingById(Long id) {
        return ratingStorage.getRatingById(id);
    }

    public List<Mpa> getAllRating() {
        return ratingStorage.getAllRating();
    }
}
