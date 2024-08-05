package ru.yandex.practicum.filmorate.service.DbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreStorage = genreDbStorage;
    }

    public Genre getGenresByID(Long id) {
        return genreStorage.getGenresById(id)
                .orElseThrow(() -> new NotFoundException("Нет жанра с таким айди"));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

}
