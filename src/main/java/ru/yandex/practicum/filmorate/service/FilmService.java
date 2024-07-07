package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void add(Film film) {
        validate(film);
        filmStorage.add(film);
    }

    public void update(Film film) {
        if (filmStorage.findById(film.getId()) == null) {
            log.error("Фильм с идентефикатором : {}", film.getId() + " не найден");
            return;
        }
        validate(film);

        filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmByID(Long filmId) {
        checkContains(filmId);

        return filmStorage.findById(filmId);
    }

    public void likeFilm(Long id, Long userId) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не существует");
        }

        checkContains(id);

        filmStorage.likeFilm(id, userId);
    }

    public void unlikeFilm(Long id, Long userId) {

        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не существует");
        }
        checkContains(id);

        filmStorage.unlikeFilm(id, userId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.findAll().stream()
                .filter(n -> n.getLikes() != null)
                .sorted((n, f) -> f.getLikes().size() - n.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkContains(Long filmId) {

        if (filmStorage.findById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if ((film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895", formatter)))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration().getSeconds() < 0) {
            log.error("Продолжительность не может быть отрицательной");
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}
