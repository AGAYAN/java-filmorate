package ru.yandex.practicum.filmorate.service.DbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmStorage = filmDbStorage;
    }

    public Film addNewFilm(Film film) {
        checkValidation(film);
        return filmStorage.add(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.findById(id);
    }

    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.findAll();
    }

    public List<Film> getPopularFilm(Long limit) {
        return filmStorage.getPopularFilm(limit);
    }

    public Film updateFilm(Film film) {
        checkValidation(film);
        return filmStorage.update(film);
    }

    public void takeLike(Long id, Long userId) {
        filmStorage.takeLike(id, userId);
    }

    public void deleteFilmById(Long id) {
        filmStorage.deleteFilm(id);
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
    }

    private void checkValidation(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895", formatter))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.error("Продолжительность должна быть положительной");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getMpa() != null && (film.getMpa().getId() <= 0 || film.getMpa().getId() > 5)) {
            log.error("Некорректный рейтинг MPA: {}", film.getMpa().getId());
            throw new ValidationException("Некорректный рейтинг MPA");
        }
        if (film.getGenres() != null) {
            List<Long> invalidGenres = film.getGenres().stream().map(Genre::getId).filter(id -> id <= 0 || id > 6).toList();
            if (!invalidGenres.isEmpty()) {
                log.error("Некорректные жанры: {}", invalidGenres);
                throw new ValidationException("Некорректные жанры");
            }
        }
    }
}